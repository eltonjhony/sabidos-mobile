package com.sabidos.presentation.onboarding.views

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import com.sabidos.R
import com.sabidos.infrastructure.extensions.showKeyboard
import com.sabidos.infrastructure.logging.Logger
import com.sabidos.presentation.components.BaseComponent
import kotlinx.android.synthetic.main.sabidos_verification_code_component.view.*

class VerificationCodeComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_verification_code_component,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    private var numTemp: String = ""

    var onCodeNotFilledCallback: (() -> Unit)? = null
    var onCodeFilledCallback: (() -> Unit)? = null
    var onBackPressedCallback: (() -> Unit)? = null

    private val inputItems: ArrayList<VerificationCodeInputItem> = ArrayList(NUM_OF_DIGITS)

    companion object {
        const val NUM_OF_DIGITS = 6
    }

    fun setup() {
        context.showKeyboard()

        for (index in 0 until (layoutContent.childCount)) {
            val view: View = layoutContent.getChildAt(index)
            if (view is VerificationCodeInputItem) {
                inputItems.add(index, view)
            }
        }

        inputItems.forEachIndexed { i, item ->
            item.addTextChangedListener(VerificationCodeEventWatcher(i, item))
            item.onKeyListener { _, keyCode, event ->
                runCommandFor(keyCode, event, i)
            }
        }

        inputItems[0].setFocus()
    }

    fun code(): String = firstCodeNumberComponent.value() +
            secondCodeNumberComponent.value() +
            thirdCodeNumberComponent.value() +
            fourCodeNumberComponent.value() +
            fiveCodeNumberComponent.value() +
            sixCodeNumberComponent.value()

    private fun triggerCodeCompletedCallbacks() {
        when {
            isAllCodeFilled() -> onCodeFilledCallback?.invoke()
            else -> onCodeNotFilledCallback?.invoke()
        }
    }

    private fun isAllCodeFilled(): Boolean = firstCodeNumberComponent.hasValue() &&
            secondCodeNumberComponent.hasValue() &&
            thirdCodeNumberComponent.hasValue() &&
            fourCodeNumberComponent.hasValue() &&
            fiveCodeNumberComponent.hasValue() &&
            sixCodeNumberComponent.hasValue()

    private fun runCommandFor(
        keyCode: Int,
        event: KeyEvent,
        i: Int
    ): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
            //backspace
            inputItems[i].setUnSelectedBackground()
            if (i != 0) { //Don't implement for first digit
                inputItems[i - 1].setFocus()
                inputItems[i - 1].setSelection()
            }
        } else if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
            onBackPressedCallback?.invoke()
        }
        return false
    }

    inner class VerificationCodeEventWatcher(
        private val i: Int,
        private val item: VerificationCodeInputItem
    ) : TextWatcher {

        override fun afterTextChanged(s: Editable?) {
            runCatching {
                if (s === item.editableText()) {

                    if (s.isBlank()) {
                        triggerCodeCompletedCallbacks()
                        return
                    }

                    if (s.length >= 2) {
                        val newTemp = s.toString().substring(s.length - 1, s.length)
                        if (newTemp != numTemp) {
                            item.setText(newTemp)
                        } else {
                            item.setText(
                                s.toString().substring(0, s.length - 1)
                            )
                        }
                    } else if (i != inputItems.size - 1) {
                        inputItems[i + 1].requestFocus()
                        inputItems[i + 1].setSelection()
                        inputItems[i].setSelectedBackground()
                    } else {
                        inputItems[i].setSelectedBackground()
                    }
                    triggerCodeCompletedCallbacks()
                }
            }.onFailure {
                Logger.withTag(VerificationCodeComponent::class.java.simpleName).withCause(it)
                onCodeFilledCallback?.invoke()
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            numTemp = s.toString()
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    }

}