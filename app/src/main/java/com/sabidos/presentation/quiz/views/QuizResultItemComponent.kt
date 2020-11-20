package com.sabidos.presentation.quiz.views

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import com.sabidos.R
import com.sabidos.infrastructure.extensions.hide
import com.sabidos.presentation.components.BaseComponent
import kotlinx.android.synthetic.main.sabidos_quiz_result_item_component.view.*

class QuizResultItemComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_quiz_result_item_component,
    R.styleable.QuizResultItemComponent,
    context,
    attrs,
    defStyleAttr
) {

    var resultValue: String = ""
        set(value) {
            field = value
            setupElement()

        }

    var includeTopSeparator = false

    override fun setCustomAttributes(attributeSet: TypedArray) {
        super.setCustomAttributes(attributeSet)
        roundCorrectLabel.text =
            attributeSet.getString(R.styleable.QuizResultItemComponent_resultLabel)
        includeTopSeparator =
            attributeSet.getBoolean(R.styleable.QuizResultItemComponent_includeTopSeparator, false)
    }

    private fun setupElement() {
        roundCorrectValue.text = resultValue
        if (!includeTopSeparator) {
            firstSeparator.hide()
        }
    }

}