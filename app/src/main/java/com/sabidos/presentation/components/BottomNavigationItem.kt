package com.sabidos.presentation.components

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.core.graphics.drawable.DrawableCompat
import com.sabidos.R
import com.sabidos.infrastructure.extensions.applyTextAppearance
import com.sabidos.infrastructure.extensions.color
import com.sabidos.infrastructure.extensions.dpToPx
import kotlinx.android.synthetic.main.sabidos_bottom_navigation_regular_item.view.*

class BottomNavigationItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_bottom_navigation_regular_item,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    var isTapped: Boolean = false

    var onClickCallback: (() -> Unit)? = null

    override fun setupComponent() {
        super.setupComponent()
        navItemContainer.setOnClickListener {
            if (!isTapped) {
                onClickCallback?.invoke()
                itemPressed()
            }
        }
    }

    fun tap() = navItemContainer.performClick()

    fun setIcon(drawable: Drawable?) {
        iconImageView.setImageDrawable(drawable)
    }

    fun setLabel(label: String) {
        labelTextView.text = label
    }

    fun itemUnPressed() {
        DrawableCompat.setTint(
            iconImageView.drawable,
            context.color(R.color.colorIconDisabled)
        )
        labelTextView.applyTextAppearance(R.style.TextStyleLight)
        labelTextView.setTextColor(context.color(R.color.colorIconDisabled))
        iconImageView.layoutParams.width = context.dpToPx(24f)
        iconImageView.layoutParams.height = context.dpToPx(24f)
        iconImageView.requestLayout()
    }

    private fun itemPressed() {
        DrawableCompat.setTint(
            iconImageView.drawable,
            context.color(R.color.colorPrimary)
        )
        labelTextView.applyTextAppearance(R.style.TextStyleMedium)
        iconImageView.layoutParams.width = context.dpToPx(28f)
        iconImageView.layoutParams.height = context.dpToPx(28f)
        iconImageView.requestLayout()
        labelTextView.setTextColor(context.color(R.color.colorPrimary))
    }

}