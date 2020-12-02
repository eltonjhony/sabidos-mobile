package com.sabidos.presentation.quiz.views

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.animation.AnimationUtils
import com.sabidos.R
import com.sabidos.data.local.singleton.QuizResult
import com.sabidos.data.local.singleton.RoundPerformance
import com.sabidos.infrastructure.extensions.color
import com.sabidos.infrastructure.logging.Logger
import com.sabidos.presentation.components.BaseComponent
import com.sabidos.presentation.quiz.QuizResultActivity
import kotlinx.android.synthetic.main.sabidos_quiz_result_content_component.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuizResultContentComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_quiz_result_content_component,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    var onNextButtonClicked: (() -> Unit)? = null
        set(value) {
            field = value
            nextButton.setOnClickListener {
                onNextButtonClicked?.invoke()
            }
        }

    fun setup(quizResult: QuizResult) {
        startViewAnimation()
        setResults(quizResult)
    }

    private fun startViewAnimation() = runCatching {
        startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.slide_up_anim
            )
        )
    }.getOrElse { Logger.withTag(QuizResultActivity::class.java.simpleName).withCause(it) }

    private fun setResults(quizResult: QuizResult) {
        nextButton.text = context.getString(R.string.next_label).toUpperCase()

        roundFeedbackLabel.text = when (quizResult.getPerformance()) {
            RoundPerformance.PERFECT -> context.getString(R.string.perfect_round_feedback)
            RoundPerformance.HIGH -> context.getString(R.string.high_round_feedback)
            RoundPerformance.MEDIUM -> context.getString(R.string.medium_round_feedback)
            RoundPerformance.LOW -> context.getString(R.string.low_round_feedback)
        }

        correctQuizValueComponent.resultValue = "${quizResult.numberOfCorrects}"
        responseTimeValueComponent.resultValue = "${quizResult.getAverageResponseTime()}s"
        xpValueComponent.resultValue = "${quizResult.getXPsForRound()}"
        startSeekProgress(quizResult)
    }

    @SuppressLint("SetTextI18n")
    private fun startSeekProgress(quizResult: QuizResult) {

        val progressColor = when (quizResult.getPerformance()) {
            RoundPerformance.PERFECT, RoundPerformance.HIGH -> context.color(R.color.greenCorrectAnswerColor)
            RoundPerformance.MEDIUM -> context.color(R.color.yellowMediumPerformanceColor)
            RoundPerformance.LOW -> context.color(R.color.redIncorrectAnswerColor)
        }

        circularTimerSeekView.setupComponent(
            progressWidth = 24f,
            arcWidth = 0f,
            progressColor = progressColor,
            max = 100
        )

        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val animator = ValueAnimator.ofInt(0, quizResult.getResultPercentageValue())
                animator.duration = 2000L
                animator.addUpdateListener { animation ->
                    GlobalScope.launch {
                        withContext(Dispatchers.Main) {
                            val value = animation.animatedValue as Int
                            circularTimerSeekView.setPoints(value)
                            correctPercentageLabel.text = "${value}%"
                        }
                    }
                }
                withContext(Dispatchers.Main) {
                    animator.start()
                }
            }
        }
    }

}