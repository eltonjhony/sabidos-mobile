package com.sabidos.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.DrawableRes
import com.sabidos.R
import com.sabidos.infrastructure.extensions.drawable
import kotlinx.android.synthetic.main.sabidos_generic_error_reload_view.view.*

class GenericErrorReloadComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_generic_error_reload_view,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    fun onReloadListener(callback: (View) -> Unit) {
        reloadButton.onClickListener { callback.invoke(it) }
    }

    fun setErrorImage(@DrawableRes image: Int) {
        errorIconView.setImageDrawable(context.drawable(image))
    }

    fun setErrorMessage(message: String) {
        errorMessageView.text = message
    }

}