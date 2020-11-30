package com.sabidos.presentation.components

import android.content.Context
import android.util.AttributeSet
import com.sabidos.R
import com.sabidos.infrastructure.Constants
import com.sabidos.infrastructure.logging.Logger
import kotlinx.android.synthetic.main.sabidos_progress_animation_dots.view.*

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

    fun startAnimation(isPrimary: Boolean = true) {
        val animationFile = when {
            isPrimary -> Constants.Animation.PRIMARY_LOADING_ANIMATION_JSON
            else -> Constants.Animation.SECONDARY_LOADING_ANIMATION_JSON
        }

        runCatching {
            animationView.setAnimation(animationFile)
            animationView.playAnimation()
        }.onFailure {
            Logger.withTag(ProgressAnimationView::class.java.simpleName).withCause(it)
        }
    }

    fun stopAnimation() {
        runCatching {
            animationView.cancelAnimation()
        }.onFailure {
            Logger.withTag(ProgressAnimationView::class.java.simpleName).withCause(it)
        }
    }

}