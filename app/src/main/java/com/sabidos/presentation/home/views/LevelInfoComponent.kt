package com.sabidos.presentation.home.views

import android.content.Context
import android.util.AttributeSet
import com.sabidos.R
import com.sabidos.presentation.components.BaseComponent
import kotlinx.android.synthetic.main.sabidos_level_information_component.view.*

class LevelInfoComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_level_information_component,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    fun setup(level: Int?, stars: Int?) {
        levelStatusComponent.level = level
        configureStarts(stars)
    }

    private fun configureStarts(stars: Int?) {
        levelStarsComponent.stars = stars
    }

}