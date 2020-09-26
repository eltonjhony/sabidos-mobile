package com.sabidos.presentation.components

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import com.sabidos.R
import com.sabidos.infrastructure.events.BottomNavigationEvent
import com.sabidos.infrastructure.events.EventBus
import kotlinx.android.synthetic.main.sabidos_empty_state_component.view.*

class EmptyStateComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_empty_state_component,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    override fun setupComponent() {
        super.setupComponent()

        premiumTextView.paintFlags = premiumTextView.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        rankingTextView.paintFlags = rankingTextView.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        premiumTextView.setOnClickListener { EventBus.send(BottomNavigationEvent(1)) }
        rankingTextView.setOnClickListener { EventBus.send(BottomNavigationEvent(2)) }
    }

}