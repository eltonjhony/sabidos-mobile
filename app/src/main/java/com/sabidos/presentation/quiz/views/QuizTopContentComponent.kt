package com.sabidos.presentation.quiz.views

import android.content.Context
import android.util.AttributeSet
import com.sabidos.R
import com.sabidos.presentation.components.BaseComponent
import kotlinx.android.synthetic.main.sabidos_quiz_top_content_component.view.*

class QuizTopContentComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_quiz_top_content_component,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    var didBackPressed: (() -> Unit)? = null
        set(value) {
            field = value
            setupBackButton()
        }

    private fun setupBackButton() {
        goBackIconView.setOnClickListener { didBackPressed?.invoke() }
    }

    var onTimerFinishedCallback: (() -> Unit)? = null
        set(value) {
            field = value
            quizProgressTimerComponent.onTimerFinishedCallback = value
        }

    fun stopTimer() {
        quizProgressTimerComponent.stop()
    }

    fun startTimer(quizLimitInSeconds: Int) {
        quizProgressTimerComponent.start(quizLimitInSeconds)
    }

    fun getTimerToAnswer() = quizProgressTimerComponent.timeToAnswer

    fun setupRoundProgress(progress: String) {
        roundProgressText.text = progress
    }
}