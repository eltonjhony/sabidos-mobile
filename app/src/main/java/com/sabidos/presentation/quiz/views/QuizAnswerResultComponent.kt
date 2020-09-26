package com.sabidos.presentation.quiz.views

import android.content.Context
import android.util.AttributeSet
import com.sabidos.R
import com.sabidos.infrastructure.Constants
import com.sabidos.infrastructure.extensions.color
import com.sabidos.infrastructure.extensions.hide
import com.sabidos.infrastructure.extensions.show
import com.sabidos.presentation.components.BaseComponent
import kotlinx.android.synthetic.main.sabidos_quiz_answer_result_component.view.*

class QuizAnswerResultComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_quiz_answer_result_component,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    override fun setupComponent() {
        super.setupComponent()
        hide()
    }

    fun showResultFor(correct: Boolean) {
        show()

        if (correct) {
            resultLabel.text = context.getString(R.string.correct_answer_result_label)
            resultLabel.setTextColor(context.color(R.color.greenCorrectAnswerColor))
            answerAnimationView.play(Constants.Animation.CORRECT_ANIMATION_JSON, 0, 1.5f) {
                hide()
            }
        } else {
            resultLabel.text = context.getString(R.string.incorrect_answer_result_label)
            resultLabel.setTextColor(context.color(R.color.redIncorrectAnswerColor))
            answerAnimationView.play(Constants.Animation.INCORRECT_ANIMATION_JSON, 0, 1.5f) {
                hide()
            }
        }
    }

}