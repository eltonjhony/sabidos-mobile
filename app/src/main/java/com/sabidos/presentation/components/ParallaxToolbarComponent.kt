package com.sabidos.presentation.components

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import com.sabidos.R
import com.sabidos.domain.UserAvatar
import kotlinx.android.synthetic.main.sabidos_parallax_toolbar_component.view.*

class ParallaxToolbarComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_parallax_toolbar_component,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    fun setupFor(avatar: UserAvatar?) {
        avatarComponent.userAvatar = avatar
    }

    fun setup(@DrawableRes resId: Int) {
        avatarComponent.drawable = resId
    }

}