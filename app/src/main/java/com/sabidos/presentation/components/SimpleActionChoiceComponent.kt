package com.sabidos.presentation.components

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import com.sabidos.R
import kotlinx.android.synthetic.main.sabidos_simple_action_choice_component.view.*

class SimpleActionChoiceComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_simple_action_choice_component,
    R.styleable.SimpleActionChoiceComponent,
    context,
    attrs,
    defStyleAttr
) {

    override fun setCustomAttributes(attributeSet: TypedArray) {
        super.setCustomAttributes(attributeSet)
        actionTitleLabel.text =
            attributeSet.getString(R.styleable.SimpleActionChoiceComponent_actionTitle)
    }

    fun onClickListener(callback: (View) -> Unit) {
        containerLayout.setOnClickListener { callback(it) }
    }

    fun enable() {
        containerLayout.isEnabled = true
    }

    fun disabled() {
        containerLayout.isEnabled = false
    }

}