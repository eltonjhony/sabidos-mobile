package com.sabidos.presentation.home.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.sabidos.R
import com.sabidos.domain.WeeklyHits
import com.sabidos.infrastructure.extensions.hide
import com.sabidos.infrastructure.extensions.show
import com.sabidos.infrastructure.logging.Logger
import com.sabidos.presentation.components.BaseComponent
import com.sabidos.presentation.components.SimpleFilterComponent.FilterModel
import im.dacer.androidcharts.LineView
import kotlinx.android.synthetic.main.sabidos_my_weekly_chart_component.view.*
import java.util.*

class MyWeeklyChartComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_my_weekly_chart_component,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    var lastWeekFilterCallback: () -> Unit = {}
        set(value) {
            field = value
            filterComponent.firstOption = FilterModel(
                context.getString(R.string.previous_label),
                filterCallback = value
            )
        }

    var currentWeekFilterCallback: () -> Unit = {}
        set(value) {
            field = value
            filterComponent.secondOption = FilterModel(
                context.getString(R.string.current_label),
                true,
                filterCallback = value
            )
        }

    override fun setupComponent() {
        super.setupComponent()
        chartProgressBar.hide()
        chartErrorComponent.hide()
    }

    fun stopLoading() {
        chartParent.show()
        chartProgressBar.stopAnimation()
        chartProgressBar.hide()
    }

    fun startLoading() {
        chartParent.hide()
        chartErrorComponent.hide()
        chartProgressBar.startAnimation()
        chartProgressBar.show()
    }

    fun setup(data: List<WeeklyHits>?) {
        chartErrorComponent.hide()
        data?.let { hits ->

            if (hits.isEmpty()) {
                chartParent.hide()
                return
            }

            runCatching {
                chart.setDrawDotLine(true)
                chart.setShowPopup(LineView.SHOW_POPUPS_All)
                chart.setBottomTextList(ArrayList(hits.map { it.date }))
                chart.setColorArray(intArrayOf(R.color.colorAccent))
                hits.mapNotNull { it.value }.let {
                    chart.setDataList(arrayListOf(ArrayList(it)))
                }
            }.onFailure { Logger.withTag(MyWeeklyChartComponent::class.java.simpleName).withCause(it) }
        }
    }

    fun setupError(reloadCallback: (View) -> Unit) {
        chartErrorComponent.onReloadListener(reloadCallback)
        chartParent.hide()
        chartErrorComponent.show()
    }

}