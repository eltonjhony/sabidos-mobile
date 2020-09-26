package com.sabidos.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import com.sabidos.R
import com.sabidos.infrastructure.logging.Logger
import kotlinx.android.synthetic.main.sabidos_progress_animation_dots.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class ProgressAnimationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    layoutId = R.layout.sabidos_progress_animation_dots,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    private lateinit var dotsModel: List<DotsModel>

    private var animating: Boolean = false

    suspend fun startAnimation(transitionTime: Long = 700L) {

        val flow = flowAnimation(transitionTime)
        flow.collect {
            it?.active(context)
            setDefaultStateExceptFor(it)
        }

    }

    private fun flowAnimation(transitionTime: Long = 700L): Flow<DotsModel?> = flow {

        animating = true
        var currentActiveDot = 1

        while (animating) {

            runCatching {

                var model = getModelBy(currentActiveDot)
                if (model != null) {
                    currentActiveDot++
                } else {
                    currentActiveDot = 1
                    model = getModelBy(currentActiveDot)
                    currentActiveDot++
                }
                emit(model)
                delay(transitionTime)

            }.onFailure {
                Logger.withTag(ProgressAnimationView::class.java.simpleName).withCause(it)
            }
        }

    }

    private fun getModelBy(position: Int) =
        dotsModel.firstOrNull { it.position == position }

    private suspend fun setDefaultStateExceptFor(model: DotsModel?) {
        dotsModel.filter { it.position != model?.position }
            .forEach { it.default(context) }
    }

    fun stopAnimation() {
        animating = false
    }

    fun setup(isDark: Boolean = false) {
        dotsModel = listOf(
            DotsModel(1, animationDotOne, isDark),
            DotsModel(2, animationDotTwo, isDark),
            DotsModel(3, animationDotThree, isDark)
        )
    }

    data class DotsModel(val position: Int, val view: ImageView, val isDark: Boolean) {

        suspend fun active(context: Context) {
            withContext(Dispatchers.Main) {
                view.setImageDrawable(
                    AppCompatResources.getDrawable(
                        context,
                        if (isDark) R.drawable.animation_circle_active_dark else R.drawable.animation_circle_active
                    )
                )
            }
        }

        suspend fun default(context: Context) {
            withContext(Dispatchers.Main) {
                view.setImageDrawable(
                    AppCompatResources.getDrawable(
                        context,
                        if (isDark) R.drawable.animation_circle_default_dark else R.drawable.animation_circle_default
                    )
                )
            }
        }

    }
}