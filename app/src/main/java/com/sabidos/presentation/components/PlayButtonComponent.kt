package com.sabidos.presentation.components

import android.content.Context
import android.util.AttributeSet
import com.sabidos.R
import kotlinx.android.synthetic.main.sabidos_play_button_component.view.*

class PlayButtonComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_play_button_component,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    var onClickCallback: (() -> Unit)? = null

    override fun setupComponent() {
        super.setupComponent()
        actionButton.setOnClickListener { onClickCallback?.invoke() }
    }
}