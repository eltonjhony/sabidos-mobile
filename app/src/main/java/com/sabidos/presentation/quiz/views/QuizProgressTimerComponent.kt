package com.sabidos.presentation.quiz.views

import android.content.Context
import android.util.AttributeSet
import com.sabidos.R
import com.sabidos.infrastructure.extensions.hide
import com.sabidos.infrastructure.logging.Logger
import com.sabidos.presentation.components.BaseComponent
import kotlinx.android.synthetic.main.sabidos_quiz_progress_timer_component.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class QuizProgressTimerComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_quiz_progress_timer_component,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    var onTimerFinishedCallback: (() -> Unit)? = null

    var responseTime = 0
        private set

    private var flowIsOpened = true
    private var job: Job? = null

    fun start(maximumTimer: Int = 15) {
        job = GlobalScope.launch {
            flowIsOpened = true
            setProgressValues(maximumTimer, maximumTimer, true)

            runCatching {
                val flow = flowTimer(maximumTimer)
                flow.collect {
                    setProgressValues(maximumTimer, it)
                }
            }.onFailure {
                if (it !is CancellationException) {
                    Logger.withTag(QuizProgressTimerComponent::class.java.simpleName).withCause(it)
                    flowIsOpened = false
                    hide()
                }
            }
        }
    }

    fun stop() {
        runCatching {
            flowIsOpened = false
            job?.cancel()
        }.onFailure {
            Logger.withTag(QuizProgressTimerComponent::class.java.simpleName).withCause(it)
        }
    }

    private fun flowTimer(maximumTimer: Int): Flow<Int> = flow {

        responseTime = maximumTimer

        while (responseTime > 0) {
            responseTime--
            delay(1000)

            if (flowIsOpened) {
                emit(responseTime)
            }
        }

        if (flowIsOpened) {
            withContext(Dispatchers.Main) {
                onTimerFinishedCallback?.invoke()
            }
        }
    }

    private suspend fun setProgressValues(
        maximumTimer: Int,
        currentTimer: Int,
        shouldSetMax: Boolean = false
    ) {
        withContext(Dispatchers.Main) {
            timerLabel.text = "$currentTimer"
            circularSeekComponent.progress = (maximumTimer.toFloat() - currentTimer.toFloat())
            if (shouldSetMax) {
                circularSeekComponent.max = maximumTimer.toFloat()
            }
        }
    }

}