package com.sabidos.presentation.quiz.views

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import com.sabidos.R
import com.sabidos.infrastructure.extensions.color
import com.sabidos.infrastructure.extensions.drawable
import com.sabidos.infrastructure.extensions.hide
import com.sabidos.infrastructure.extensions.show
import com.sabidos.infrastructure.logging.Logger
import com.sabidos.presentation.components.BaseComponent
import kotlinx.android.synthetic.main.sabidos_quiz_selection_item.view.*

class QuizSelectionItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_quiz_selection_item,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    var onClickCallback: (() -> Unit)? = null

    var questionAnswered: Boolean = false

    fun setup(optionDescription: String?) {
        percentageAnsweredTextView.hide()
        progressLineView.hide()
        optionDescTextView.text = optionDescription

        optionContainer.setOnClickListener {

            if (questionAnswered) {
                return@setOnClickListener
            }

            onClickCallback?.invoke()
        }

    }

    fun animateAnswer(percentageAnswered: Int, isCorrect: Boolean, callback: (() -> Unit)? = null) {
        percentageAnsweredTextView.show()
        progressLineView.show()

        if (isCorrect) {
            animateProgressWith(R.drawable.curved_progress_end_correct_background)
            percentageAnsweredTextView.setTextColor(
                context.color(
                    R.color.colorPrimary
                )
            )
        } else {
            animateProgressWith(R.drawable.curved_progress_end_wrong_background)
            percentageAnsweredTextView.setTextColor(
                context.color(
                    R.color.disableButtonColor
                )
            )
            optionDescTextView.setTextColor(context.color(R.color.colorWhite))
        }

        runCatching {
            animateAnswerProgress(percentageAnswered, callback)
        }.onFailure {
            Logger.withTag(QuizSelectionItem::class.java.simpleName).withCause(it)
            progressLineView.progress = 100
            percentageAnsweredTextView.text = "$percentageAnswered %"
            callback?.invoke()
        }

    }

    fun animateUnSelectedAnswer(percentageAnswered: Int) {
        percentageAnsweredTextView.show()
        progressLineView.show()
        animateProgressWith(R.drawable.curved_progress_end_unselected_background)
        percentageAnsweredTextView.setTextColor(
            context.color(
                R.color.disableButtonColor
            )
        )

        runCatching {
            animateAnswerProgress(percentageAnswered)
        }.onFailure {
            Logger.withTag(QuizSelectionItem::class.java.simpleName).withCause(it)
            progressLineView.progress = 100
            percentageAnsweredTextView.text = "$percentageAnswered %"
        }

    }

    private fun animateAnswerProgress(percentageAnswered: Int, callback: (() -> Unit)? = null) {
        runCatching {
            val handler = Handler(Looper.getMainLooper())
            var progressStatus = 0
            Thread(Runnable {
                while (progressStatus < percentageAnswered) {
                    progressStatus += 1
                    handler.post {
                        progressLineView.progress = progressStatus
                        percentageAnsweredTextView.text = "$progressStatus %"
                    }
                    try {
                        Thread.sleep(8)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
                handler.post {
                    callback?.invoke()
                }
            }).start()
        }.getOrThrow()
    }

    private fun animateProgressWith(drawable: Int) {
        progressLineView.progressDrawable = context.drawable(drawable)
    }

}