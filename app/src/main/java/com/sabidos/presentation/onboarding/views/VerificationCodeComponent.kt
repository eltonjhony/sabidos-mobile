package com.sabidos.presentation.onboarding.views

import android.content.Context
import android.util.AttributeSet
import com.sabidos.R
import com.sabidos.infrastructure.extensions.showKeyboard
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

    var onCodeNotFilledCallback: (() -> Unit)? = null
    var onCodeFilledCallback: (() -> Unit)? = null
    var onBackPressedCallback: (() -> Unit)? = null

    fun setup() {
        context.showKeyboard()
        firstCodeNumberComponent.setFocus()

        firstCodeNumberComponent.viewModel =
            VerificationCodeInputItem.ViewModel(
                onAddCallback = {
                    secondCodeNumberComponent.setFocus()
                    triggerCodeCompletedCallbacks()
                },
                onBackPressedCallback = onBackPressedCallback
            )

        secondCodeNumberComponent.viewModel =
            VerificationCodeInputItem.ViewModel(
                onAddCallback = {
                    thirdCodeNumberComponent.setFocus()
                    triggerCodeCompletedCallbacks()
                },
                onDeleteCallback = {
                    firstCodeNumberComponent.setFocus()
                    triggerCodeCompletedCallbacks()
                },
                onBackPressedCallback = onBackPressedCallback
            )

        thirdCodeNumberComponent.viewModel =
            VerificationCodeInputItem.ViewModel(
                onAddCallback = {
                    fourCodeNumberComponent.setFocus()
                    triggerCodeCompletedCallbacks()
                },
                onDeleteCallback = {
                    secondCodeNumberComponent.setFocus()
                    triggerCodeCompletedCallbacks()
                },
                onBackPressedCallback = onBackPressedCallback
            )

        fourCodeNumberComponent.viewModel =
            VerificationCodeInputItem.ViewModel(
                onAddCallback = {
                    fiveCodeNumberComponent.setFocus()
                    triggerCodeCompletedCallbacks()
                },
                onDeleteCallback = {
                    thirdCodeNumberComponent.setFocus()
                    triggerCodeCompletedCallbacks()
                },
                onBackPressedCallback = onBackPressedCallback
            )

        fiveCodeNumberComponent.viewModel =
            VerificationCodeInputItem.ViewModel(
                onAddCallback = {
                    sixCodeNumberComponent.setFocus()
                    triggerCodeCompletedCallbacks()
                },
                onDeleteCallback = {
                    fourCodeNumberComponent.setFocus()
                    triggerCodeCompletedCallbacks()
                },
                onBackPressedCallback = onBackPressedCallback
            )

        sixCodeNumberComponent.viewModel =
            VerificationCodeInputItem.ViewModel(
                onAddCallback = {
                    sixCodeNumberComponent.setFocus()
                    triggerCodeCompletedCallbacks()
                },
                onDeleteCallback = {
                    fiveCodeNumberComponent.setFocus()
                    triggerCodeCompletedCallbacks()
                },
                onBackPressedCallback = onBackPressedCallback
            )

    }

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

    fun code(): String = firstCodeNumberComponent.value() +
            secondCodeNumberComponent.value() +
            thirdCodeNumberComponent.value() +
            fourCodeNumberComponent.value() +
            fiveCodeNumberComponent.value() +
            sixCodeNumberComponent.value()

}