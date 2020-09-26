package com.sabidos.presentation.onboarding.views

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import com.sabidos.R
import com.sabidos.infrastructure.extensions.drawable
import com.sabidos.presentation.components.BaseComponent
import kotlinx.android.synthetic.main.sabidos_verification_code_input_item.view.*

class VerificationCodeInputItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_verification_code_input_item,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    var viewModel: ViewModel? = null
        set(value) {
            field = value
            setup()
        }

    fun setFocus() {
        verificationField.requestFocus()
        verificationField.setSelection(verificationField.text?.length ?: 0)
    }

    fun hasValue(): Boolean = verificationField.text?.isNotBlank() ?: false

    fun value(): String = verificationField.text?.toString() ?: ""

    private fun setup() {

        verificationField.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                runCommandFor(keyCode)
            }
            true
        }

    }

    private fun runCommandFor(keyCode: Int) {
        when (keyCode) {
            KeyEvent.KEYCODE_DEL -> {
                verificationField.text?.clear()
                verificationField.background = context.drawable(R.drawable.edit_text_border)
                viewModel?.onDeleteCallback?.invoke()
            }
            KeyEvent.KEYCODE_BACK -> {
                viewModel?.onBackPressedCallback?.invoke()
            }
            else -> {
                getCodeFrom(keyCode)?.let {
                    verificationField.setText(it)
                    verificationField.background =
                        context.drawable(R.drawable.edit_text_selected_border)
                    viewModel?.onAddCallback?.invoke()
                }
            }
        }
    }

    private fun getCodeFrom(keyCode: Int): String? {
        return when (keyCode) {
            7 -> "0"
            8 -> "1"
            9 -> "2"
            10 -> "3"
            11 -> "4"
            12 -> "5"
            13 -> "6"
            14 -> "7"
            15 -> "8"
            16 -> "9"
            else -> null
        }
    }

    data class ViewModel(
        val onAddCallback: (() -> Unit)? = null,
        val onDeleteCallback: (() -> Unit)? = null,
        val onBackPressedCallback: (() -> Unit)? = null
    )
}