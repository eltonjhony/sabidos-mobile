package com.sabidos.presentation.onboarding.views

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
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

    fun setFocus() {
        verificationField.requestFocus()
        verificationField.setSelection(verificationField.text?.length ?: 0)
    }

    fun hasValue(): Boolean = verificationField.text?.isNotBlank() ?: false

    fun value(): String = verificationField.text?.toString() ?: ""

    fun addTextChangedListener(textWatcher: TextWatcher) {
        verificationField.addTextChangedListener(textWatcher)
    }

    fun onKeyListener(listener: (View, Int, KeyEvent) -> Boolean) {
        verificationField.setOnKeyListener(listener)
    }

    fun editableText(): Editable =
        verificationField.editableText

    fun setText(newTemp: String) {
        verificationField.setText(newTemp)
    }

    fun setSelection() {
        verificationField.setSelection(verificationField.length())
    }

    fun setSelectedBackground() {
        verificationField.background = context.drawable(R.drawable.edit_text_selected_border)
    }

    fun setUnSelectedBackground() {
        verificationField.background = context.drawable(R.drawable.edit_text_border)
    }

}