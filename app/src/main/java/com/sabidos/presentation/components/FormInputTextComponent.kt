package com.sabidos.presentation.components

import android.content.Context
import android.content.res.TypedArray
import android.text.InputType
import android.util.AttributeSet
import androidx.core.view.isVisible
import com.sabidos.R
import com.sabidos.infrastructure.extensions.drawable
import com.sabidos.infrastructure.extensions.invisible
import com.sabidos.infrastructure.extensions.onChange
import com.sabidos.infrastructure.extensions.show
import kotlinx.android.synthetic.main.sabidos_form_text_component.view.*

class FormInputTextComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_form_text_component,
    R.styleable.FormInputTextComponent,
    context,
    attrs,
    defStyleAttr
) {

    val value: String
        get() {
            return formTextInputField.text.toString()
        }

    override fun setupComponent() {
        super.setupComponent()
        hideInputError()
    }

    override fun setCustomAttributes(attributeSet: TypedArray) {
        formTitleText.text = attributeSet.getString(R.styleable.FormInputTextComponent_titleText)
        formTextInputField.hint =
            attributeSet.getString(R.styleable.FormInputTextComponent_hintDescription)

        if (!attributeSet.getBoolean(R.styleable.FormInputTextComponent_optionalField, false)) {
            requiredSymbol.text = "*"
        }

        when (attributeSet.getInt(R.styleable.FormInputTextComponent_inputTypeNumber, 0)) {
            1 -> formTextInputField.inputType = InputType.TYPE_CLASS_PHONE
        }

    }

    fun onChange(callback: (String) -> Unit) {
        formTextInputField.onChange { callback(it) }
    }

    fun setValue(value: String = "") {
        formTextInputField.isSaveEnabled = false
        formTextInputField.setText(value)
    }

    fun showInputErrorWith(message: String) {
        formTextInputField.background = context.drawable(R.drawable.edit_text_border_error)
        formTextInputErrorField.text = message
        formTextInputErrorField.show()
    }

    fun hideInputError() {
        if (formTextInputErrorField.isVisible) {
            formTextInputErrorField.invisible()
            formTextInputField.background =
                context.drawable(R.drawable.edit_text_border)
        }
    }

}