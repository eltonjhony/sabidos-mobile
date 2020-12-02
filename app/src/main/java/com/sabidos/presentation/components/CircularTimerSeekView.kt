package com.sabidos.presentation.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import com.sabidos.R
import com.sabidos.infrastructure.extensions.color
import com.sabidos.infrastructure.logging.Logger
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * CircularTimerSeekView class
 * Class modified from https://github.com/enginebai/SwagPoints/blob/master/library/src/main/java/com/dualcores/swagpoints/SwagPoints.java
 */
class CircularTimerSeekView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var mProgressPaint: Paint? = null
    private var mArcPaint: Paint? = null

    private var mIndicatorIconY = 0f
    private var mIndicatorIconX = 0f

    private var mArcRect = RectF()

    private var mArcRadius: Int = 0

    private var mTranslateY = 0f
    private var mTranslateX = 0f

    private var mProgressWidth: Float = 0f
    private var mArcWidth: Float = 0f

    private var progressColor: Int = 0
    private var arcColor: Int = 0

    private var mPoints: Int = 0
    private var max: Int = 0

    private var mProgressSweep = 0f

    private var mIndicatorIcon: Drawable? = null

    private var mUpdateTimes = 0
    private var mPreviousProgress = -1
    private var mCurrentProgress = 0

    fun setupComponent(
        customIndicatorIcon: Drawable? = null,
        indicatorSizeDelta: Int = 4,
        progressWidth: Float,
        arcWidth: Float,
        progressColor: Int = context.color(R.color.colorWhite),
        arcColor: Int = context.color(R.color.disableButtonColor),
        max: Int
    ) {

        val density = resources.displayMetrics.density

        //Default values
        mProgressWidth *= density
        mArcWidth *= density

        mIndicatorIcon = customIndicatorIcon

        mIndicatorIcon?.let { indicator ->
            val indicatorIconHalfWidth = indicator.intrinsicWidth / indicatorSizeDelta
            val indicatorIconHalfHeight = indicator.intrinsicHeight / indicatorSizeDelta
            indicator.setBounds(
                -indicatorIconHalfWidth,
                -indicatorIconHalfHeight,
                indicatorIconHalfWidth,
                indicatorIconHalfHeight
            )
        }

        mProgressWidth = progressWidth
        mArcWidth = arcWidth

        this.progressColor = progressColor
        this.arcColor = arcColor
        this.max = max

        mArcPaint = Paint().apply {
            color = arcColor
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = mArcWidth
        }

        mProgressPaint = Paint().apply {
            color = progressColor
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = mProgressWidth
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        runCatching {
            val width: Int = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
            val height: Int = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
            val min: Int = min(width, height)

            mTranslateX = (width * 0.5f)
            mTranslateY = (height * 0.5f)

            val arcDiameter: Int = min - paddingLeft
            mArcRadius = arcDiameter / 2

            val top: Float = ((height / 2) - (arcDiameter / 2)).toFloat()
            val left: Float = ((width / 2) - (arcDiameter / 2)).toFloat()

            mArcRect.set(left, top, left + arcDiameter, top + arcDiameter)

            updateIndicatorIconPosition()
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }.onFailure {
            Logger.withTag(CircularTimerSeekView::class.java.simpleName).withCause(it)
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        runCatching {
            canvas?.let { safeCanvas ->

                //draw the arc and progress
                mArcPaint?.let {
                    safeCanvas.drawArc(mArcRect, ANGLE_OFFSET, 360f, false, it)
                }
                mProgressPaint?.let {
                    safeCanvas.drawArc(mArcRect, ANGLE_OFFSET, mProgressSweep, false, it)
                }

                mIndicatorIcon?.let { indicator ->
                    //draw the indicator icon
                    safeCanvas.translate(
                        mTranslateX - mIndicatorIconX,
                        mTranslateY - mIndicatorIconY
                    )
                    indicator.draw(safeCanvas)
                }

            }
        }.onFailure {
            Logger.withTag(CircularTimerSeekView::class.java.simpleName).withCause(it)
        }
    }

    fun setPoints(points: Int) {
        mPoints = if (points > max) max else points
        updateProgress(mPoints)
    }

    private fun updateProgress(progress: Int) {
        mUpdateTimes++

        if (mUpdateTimes == 1) {
            mCurrentProgress = progress
        } else {
            mPreviousProgress = mCurrentProgress
            mCurrentProgress = progress
        }

        mPoints = progress
        mProgressSweep = (progress / valuePerDegree())
        updateIndicatorIconPosition()
        invalidate()
    }

    private fun valuePerDegree(): Float = max / 360.0f

    private fun updateIndicatorIconPosition() {
        val thumbAngle = (mProgressSweep + 90)
        mIndicatorIconX = (mArcRadius * cos(Math.toRadians(thumbAngle.toDouble()))).toFloat()
        mIndicatorIconY = (mArcRadius * sin(Math.toRadians(thumbAngle.toDouble()))).toFloat()
    }

    companion object {
        const val ANGLE_OFFSET = -90f
    }

}