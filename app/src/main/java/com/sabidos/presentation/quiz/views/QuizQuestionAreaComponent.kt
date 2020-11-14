package com.sabidos.presentation.quiz.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import com.sabidos.R
import com.sabidos.domain.QuizItem
import com.sabidos.infrastructure.extensions.hide
import com.sabidos.infrastructure.extensions.load
import com.sabidos.infrastructure.extensions.show
import com.sabidos.presentation.components.BaseComponent
import kotlinx.android.synthetic.main.sabidos_quiz_question_area_component.view.*

class QuizQuestionAreaComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_quiz_question_area_component,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    override fun setupComponent() {
        super.setupComponent()
        questionAreaLayout.hide()
        questionAreaWithImageLayout.hide()
    }

    fun setup(quizItem: QuizItem) {

        categoryTextView.text = quizItem.category.description
        categoryTextView.setTextColor(Color.parseColor(quizItem.category.colorHex))

        if (quizItem.imageUrl != null) {
            questionAreaLayout.hide()
            questionAreaWithImageLayout.show()
            questionDescImgTextView.text = quizItem.description
            quizImage.load(quizItem.imageUrl)
        } else {
            questionAreaLayout.show()
            questionAreaWithImageLayout.hide()
            questionDescTextView.text = quizItem.description
        }
    }
}