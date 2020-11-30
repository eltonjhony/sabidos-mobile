package com.sabidos.presentation.home.views

import android.content.Context
import android.util.AttributeSet
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.sabidos.R
import com.sabidos.infrastructure.extensions.hide
import com.sabidos.infrastructure.extensions.show
import com.sabidos.presentation.home.TimelineAdapter
import com.sabidos.presentation.components.BaseComponent
import kotlinx.android.synthetic.main.sabidos_timeline_component.view.*

class TimelineComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_timeline_component,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    private lateinit var nestedScrollView: NestedScrollView
    private var isLoading = false
    private var currentPage = 1

    fun setup(timelineAdapter: TimelineAdapter, nestedScrollView: NestedScrollView) {
        this.nestedScrollView = nestedScrollView
        val linearLayoutManager = LinearLayoutManager(context)
        timelineRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = timelineAdapter
            isNestedScrollingEnabled = false
        }
        resetPagination()
    }

    fun stopLoading() {
        isLoading = false
        progressAnimationComponent.hide()
        progressAnimationComponent.stopAnimation()
    }

    fun starLoading() {
        isLoading = true
        progressAnimationComponent.show()
        progressAnimationComponent.startAnimation()
    }

    private fun resetPagination() {
        currentPage = 1
    }

    fun onLoadMore(loadListener: (nextPage: Int) -> Unit) {
        nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, _: Int, scrollY: Int, _: Int, oldScrollY: Int ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY >= v.getChildAt(v.childCount - 1)
                        .measuredHeight - v.measuredHeight &&
                    scrollY > oldScrollY
                ) {
                    if (!isLoading) {
                        currentPage++
                        loadListener(currentPage)
                    }
                }
            }
        }
    }

}