package com.sabidos.presentation.components

import android.content.Context
import android.util.AttributeSet
import com.sabidos.R
import com.sabidos.infrastructure.extensions.drawable
import kotlinx.android.synthetic.main.sabidos_level_status_component.view.*

class LevelStatusComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_level_status_component,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    var level: Int? = 0
        set(value) {
            field = value
            configureLevel(level)
        }

    private fun configureLevel(level: Int?) {
        level?.let {
            when (level) {
                1 -> levelStatusIcon.setImageDrawable(
                    context.drawable(
                        R.mipmap.ic_blue_symbol
                    )
                )
                2 -> levelStatusIcon.setImageDrawable(
                    context.drawable(
                        R.mipmap.ic_gold_symbol
                    )
                )
                3 -> levelStatusIcon.setImageDrawable(
                    context.drawable(
                        R.mipmap.ic_black_symbol
                    )
                )
                4 -> levelStatusIcon.setImageDrawable(
                    context.drawable(
                        R.mipmap.ic_prime_symbol
                    )
                )
            }
        } ?: setupEmptyState()
    }

    private fun setupEmptyState() = levelStatusIcon.setImageDrawable(
        context.drawable(
            R.mipmap.ic_error_symbol
        )
    )
}