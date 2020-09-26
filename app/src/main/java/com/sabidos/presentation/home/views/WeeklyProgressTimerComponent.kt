package com.sabidos.presentation.home.views

import android.content.Context
import android.util.AttributeSet
import com.sabidos.R
import com.sabidos.infrastructure.extensions.getWeeklyRankingProgressFormatted
import com.sabidos.infrastructure.extensions.hoursInWeek
import com.sabidos.infrastructure.extensions.today
import com.sabidos.infrastructure.logging.Logger
import com.sabidos.presentation.components.BaseComponent
import kotlinx.android.synthetic.main.content_home_weekly_timers.view.*
import kotlinx.android.synthetic.main.sabidos_progress_timer_component.view.*
import kotlinx.coroutines.*
import java.util.*

class WeeklyProgressTimerComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_progress_timer_component,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    var endDate: Calendar = today()
        set(value) {
            field = value
            runCatching {
                GlobalScope.launch { setup() }
            }.onFailure {
                Logger.withTag(WeeklyProgressTimerComponent::class.java.simpleName).withCause(it)
            }
        }

    private suspend fun setup() = withContext(Dispatchers.IO) {
        weeklyProgressTimer.circularSeekComponent.max = hoursInWeek

        val consumedHours = getWeeklyRankingProgressFormatted(endDate)

        delay(200)
        for (x in 1 until consumedHours) {
            animate(x)
            delay(50)
        }

    }

    private suspend fun animate(x: Long) {
        withContext(Dispatchers.Main) {
            weeklyProgressTimer.circularSeekComponent.progress = x.toFloat()
        }
    }

}