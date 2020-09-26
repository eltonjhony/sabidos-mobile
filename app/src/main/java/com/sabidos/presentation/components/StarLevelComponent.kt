package com.sabidos.presentation.components

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.sabidos.R
import com.sabidos.infrastructure.extensions.drawable
import com.sabidos.infrastructure.extensions.hide
import kotlinx.android.synthetic.main.sabidos_star__level_component.view.*

class StarLevelComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_star__level_component,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    private val model = listOf(
        StarLevelModel(1, firstStar),
        StarLevelModel(2, secondStar),
        StarLevelModel(3, thirdStar),
        StarLevelModel(4, fourthStar),
        StarLevelModel(5, fiftyStar)
    )

    private val limit = model.size

    var stars: Int? = 0
        set(value) {
            field = value
            setup()
        }

    private fun setup() {
        stars?.let { iterateToEnableStars(it) } ?: setupEmptyState()
    }

    private fun iterateToEnableStars(numberOfStars: Int) {

        var range = numberOfStars

        if (numberOfStars > limit) {
            range = limit
        }

        for (x in 0 until range) {
            model[x].enabled()
        }
    }

    private fun setupEmptyState() {
        hide()
    }

    data class StarLevelModel(val position: Int, val icon: AppCompatImageView) {

        init {
            disabled()
        }

        fun enabled() =
            icon.setImageDrawable(
                icon.context.drawable(
                    R.drawable.ic_enabled_star
                )
            )

        private fun disabled() =
            icon.setImageDrawable(
                icon.context.drawable(
                    R.drawable.ic_disable_star
                )
            )
    }

}