package com.sabidos.presentation.profile.views

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager
import com.sabidos.infrastructure.logging.Logger

class WrapContentHeightViewPager : ViewPager {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(
        context,
        attrs
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        runCatching {
            var height = 0
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                child.measure(
                    widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                )
                val h = child.measuredHeight
                if (h > height) height = h
            }
            super.onMeasure(
                widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
            )
        }.onFailure {
            Logger.withTag(WrapContentHeightViewPager::class.java.simpleName).withCause(it)
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}