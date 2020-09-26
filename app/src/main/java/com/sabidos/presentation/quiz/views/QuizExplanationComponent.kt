package com.sabidos.presentation.quiz.views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import com.sabidos.R
import com.sabidos.domain.Alternative
import com.sabidos.domain.Explanation
import com.sabidos.infrastructure.extensions.color
import com.sabidos.infrastructure.extensions.dpToPx
import com.sabidos.infrastructure.extensions.hide
import com.sabidos.infrastructure.extensions.stylableText
import com.sabidos.presentation.components.BaseComponent
import kotlinx.android.synthetic.main.sabidos_quiz_explanation_component.view.*

class QuizExplanationComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_quiz_explanation_component,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    fun configureView(explanation: Explanation, correctAlternative: Alternative) {
        hide()
        correctAnswerResultLabel.stylableText(
            context.getString(R.string.long_correct_answer_result_label),
            correctAlternative.description
        ) { ds ->
            ds.isUnderlineText = false
            ds.textSize = context.dpToPx(16f).toFloat()
            ds.typeface = Typeface.create(ds.typeface, Typeface.BOLD)
            ds.color = context.color(R.color.colorPrimary)
        }
        questionExplanationTextView.text = explanation.description
        explanationResourceTextView.text = explanation.resource
    }

}