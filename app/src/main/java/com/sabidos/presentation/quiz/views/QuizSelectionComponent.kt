package com.sabidos.presentation.quiz.views

import android.content.Context
import android.util.AttributeSet
import com.sabidos.R
import com.sabidos.domain.Alternative
import com.sabidos.presentation.components.BaseComponent
import kotlinx.android.synthetic.main.sabidos_quiz_selection_component.view.*

class QuizSelectionComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_quiz_selection_component,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    var isTimerOver = false

    var onSelectionCallback: ((Alternative) -> Unit)? = null
    var onDidClickCallback: ((Alternative) -> Unit)? = null

    var alternatives: List<Alternative> = emptyList()
        set(value) {
            field = value
            configureAlternatives()
        }

    private fun configureAlternatives() {
        alternatives.forEachIndexed { index, alternative ->
            val choiceComponent = getChoiceComponentBy(index)
            configureItem(index, choiceComponent, alternative)
        }
    }

    private fun configureItem(idx: Int, choice: QuizSelectionItem?, alternative: Alternative) {
        choice?.setup(
            alternative.description
        )

        choice?.onClickCallback = {

            if (!isTimerOver) {

                onDidClickCallback?.invoke(alternative)

                alternatives.forEachIndexed { index, item ->

                    if (index != idx) {
                        val choiceComponent = getChoiceComponentBy(index)
                        choiceComponent.questionAnswered = true
                        choiceComponent.animateUnSelectedAnswer(item.percentageAnswered)
                    }

                }

                choice?.questionAnswered = true
                choice?.animateAnswer(alternative.percentageAnswered, alternative.isCorrect)
                onSelectionCallback?.invoke(alternative)

            }

        }
    }

    private fun getChoiceComponentBy(index: Int): QuizSelectionItem {
        return when (index) {
            0 -> firstChoiceComponent
            1 -> secondChoiceComponent
            2 -> thirdChoiceComponent
            else -> fourthChoiceComponent
        }
    }

}