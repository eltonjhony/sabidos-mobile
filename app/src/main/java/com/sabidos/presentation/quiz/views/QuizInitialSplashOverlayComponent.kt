package com.sabidos.presentation.quiz.views

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.sabidos.R
import com.sabidos.infrastructure.extensions.hide
import com.sabidos.infrastructure.extensions.pump
import com.sabidos.infrastructure.extensions.show
import com.sabidos.infrastructure.logging.Logger
import com.sabidos.presentation.components.BaseComponent
import kotlinx.android.synthetic.main.sabidos_quiz_initial_splash_overlay_component.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class QuizInitialSplashOverlayComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_quiz_initial_splash_overlay_component,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    private var job: Job? = null

    override fun setupComponent() {
        super.setupComponent()
        quizErrorComponent.hide()
    }

    fun showNetworkErrorWithRetry(retryCallback: (View) -> Unit) {
        initialSplashLayout.hide()
        quizErrorComponent.setErrorImage(R.drawable.ic_connection_lost)
        quizErrorComponent.setErrorMessage(context.getString(R.string.internet_connection_error))
        quizErrorComponent.show()
        quizErrorComponent.onReloadListener(retryCallback)
    }

    fun showErrorWithRetry(retryCallback: (View) -> Unit) {
        initialSplashLayout.hide()
        quizErrorComponent.setErrorImage(R.drawable.ic_bored_icon)
        quizErrorComponent.show()
        quizErrorComponent.onReloadListener(retryCallback)
    }

    fun startPreparingRound(roundNumber: Int = 1) {
        initialSplashLayout.show()
        quizErrorComponent.hide()
        roundValue.text = "$roundNumber"
        startSeekProgress()
        start()
    }

    fun didPrepareRound(callback: () -> Unit) {
        GlobalScope.launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    if (job?.isActive != true) {
                        withContext(Dispatchers.Main) {
                            callback()
                        }
                    } else {
                        didPrepareRound(callback)
                    }
                }
            }.onFailure {
                Logger.withTag(QuizInitialSplashOverlayComponent::class.java.simpleName)
                    .withCause(it)
                callback()
            }
        }
    }

    private fun startSeekProgress() {
        timerView.pump()
        circularSeekComponent.max = MAX_PROGRESS_VALUE
        circularSeekComponent.progress = 0f
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val animator = ValueAnimator.ofFloat(0f, MAX_PROGRESS_VALUE)
                animator.duration = (DEFAULT_COUNTER * 1000).toLong()
                animator.addUpdateListener { animation ->
                    GlobalScope.launch {
                        withContext(Dispatchers.Main) {
                            circularSeekComponent.progress = animation.animatedValue as Float
                        }
                    }
                }
                withContext(Dispatchers.Main) {
                    animator.start()
                }
            }
        }
    }

    private fun start() {
        job = GlobalScope.launch {
            setProgressValue(DEFAULT_COUNTER)

            runCatching {
                val flow = flowTimer(DEFAULT_COUNTER)
                flow.collect {
                    setProgressValue(it)
                }
            }.onFailure {
                stopTimer()
                Logger.withTag(QuizInitialSplashOverlayComponent::class.java.simpleName)
                    .withCause(it)
            }
        }
    }

    private fun flowTimer(maximumTimer: Int): Flow<Int> = flow {

        var temp = maximumTimer

        while (temp > 0) {
            temp--
            delay(1000)
            emit(temp)
        }

        stopTimer()
    }

    private fun stopTimer() {
        job?.cancel()
        job = null
    }

    private suspend fun setProgressValue(value: Int) {
        withContext(Dispatchers.Main) {
            timerValue.text = "$value"
        }
    }

    companion object {
        const val DEFAULT_COUNTER = 3
        const val MAX_PROGRESS_VALUE = 100f
    }

}