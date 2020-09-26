package com.sabidos.presentation.components

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import com.sabidos.R
import com.sabidos.infrastructure.logging.Logger
import kotlinx.android.synthetic.main.sabidos_simple_animation_view.view.*

class SimpleAnimationComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_simple_animation_view,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    fun play(
        animationJson: String,
        repeatCount: Int? = null,
        speed: Float? = null,
        onAnimationEndCallback: () -> Unit
    ) {
        runCatching {
            animationView.setAnimation(animationJson)
            speed?.let {
                animationView.speed = it
            }
            repeatCount?.let {
                animationView.repeatCount = it
            }
            animationView.playAnimation()
            animationView.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    onAnimationEndCallback()
                }

                override fun onAnimationCancel(animation: Animator?) {

                }

                override fun onAnimationStart(animation: Animator?) {

                }
            })
        }.onFailure {
            Logger.withTag(SimpleAnimationComponent::class.java.simpleName).withCause(it)
        }
    }
}