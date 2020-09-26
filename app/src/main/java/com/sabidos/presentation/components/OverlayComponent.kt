package com.sabidos.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.sabidos.R
import com.sabidos.infrastructure.extensions.hide
import com.sabidos.infrastructure.extensions.show
import kotlinx.android.synthetic.main.sabidos_overlay_component.view.*

class OverlayComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_overlay_component,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    override fun setupComponent() {
        super.setupComponent()
        quizErrorComponent.hide()
    }

    fun showNetworkErrorWithRetry(retryCallback: (View) -> Unit) {
        quizErrorComponent.setErrorImage(R.drawable.ic_connection_lost)
        quizErrorComponent.setErrorMessage(context.getString(R.string.internet_connection_error))
        quizErrorComponent.show()
        quizErrorComponent.onReloadListener(retryCallback)
    }

    fun showErrorWithRetry(retryCallback: (View) -> Unit) {
        quizErrorComponent.setErrorImage(R.drawable.ic_bored_icon)
        quizErrorComponent.show()
        quizErrorComponent.onReloadListener(retryCallback)
    }
}