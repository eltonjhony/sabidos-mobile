package com.sabidos.presentation.home.views

import android.content.Context
import android.util.AttributeSet
import com.sabidos.R
import com.sabidos.infrastructure.extensions.getWeeklyRankingTimerFormatted
import com.sabidos.infrastructure.extensions.today
import com.sabidos.presentation.components.BaseComponent
import kotlinx.android.synthetic.main.sabidos_simple_timer_countdown_component.view.*
import java.util.*

class SimpleTimerCountdownComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_simple_timer_countdown_component,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    var endDate: Calendar = today()
    set(value) {
        field = value
        setup()
    }

    private fun setup() {
        timerText.text = getWeeklyRankingTimerFormatted(endDate)
    }

}