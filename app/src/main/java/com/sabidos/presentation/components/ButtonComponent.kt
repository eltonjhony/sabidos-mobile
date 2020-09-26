package com.sabidos.presentation.components

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import com.sabidos.R
import kotlinx.android.synthetic.main.sabidos_button_component.view.*

class ButtonComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_button_component,
    R.styleable.ButtonComponent,
    context,
    attrs,
    defStyleAttr
) {

    override fun setCustomAttributes(attributeSet: TypedArray) {
        super.setCustomAttributes(attributeSet)
        actionButton.text =
            attributeSet.getString(R.styleable.ButtonComponent_title)
    }

    fun onClickListener(callback: (View) -> Unit) {
        actionButton.setOnClickListener { callback(it) }
    }

    fun enable() {
        if (!actionButton.isEnabled) {
            actionButton.isEnabled = true
        }
    }

    fun disabled() {
        if (actionButton.isEnabled) {
            actionButton.isEnabled = false
        }
    }

    fun setText(text: String) {
        actionButton.text = text
    }

}