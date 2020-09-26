package com.sabidos.presentation.components

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.sabidos.R
import com.sabidos.infrastructure.extensions.drawable

class BottomNavigationComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_bottom_navigation_menu,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    var items = emptyList<NavModel>()
        set(value) {
            field = value
            setupItems()
        }

    private fun setupItems() {
        items.forEach {
            it.item = findViewById(it.itemId)
            it.build(context)
            it.setupListeners(items)
        }
    }

    data class NavModel(
        @IdRes val itemId: Int,
        @DrawableRes val icon: Int,
        @StringRes val label: Int,
        val isDefault: Boolean = false,
        val navigation: () -> Unit

    ) {

        var item: BottomNavigationItem? = null

        fun build(context: Context) {
            item?.setIcon(context.drawable(icon))
            item?.setLabel(context.getString(label))
        }

        fun setupListeners(models: List<NavModel>) {
            item?.onClickCallback = {
                models.forEach {
                    it.item?.isTapped = false
                    it.item?.itemUnPressed()
                }
                navigation.invoke()
                item?.isTapped = true
            }

            if (isDefault) {
                item?.tap()
            } else {
                item?.itemUnPressed()
            }

        }

    }

}