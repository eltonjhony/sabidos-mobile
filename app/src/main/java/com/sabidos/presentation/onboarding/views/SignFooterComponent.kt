package com.sabidos.presentation.onboarding.views

import android.content.Context
import android.util.AttributeSet
import com.sabidos.R
import com.sabidos.infrastructure.extensions.clickableText
import com.sabidos.presentation.components.BaseComponent
import kotlinx.android.synthetic.main.sabidos_sign_footer_component.view.*

class SignFooterComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_sign_footer_component,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    var onAnonymouslySignClickCallback: (() -> Unit)? = null

    override fun setupComponent() {
        super.setupComponent()

        anonymouslySignLink.clickableText(
            context.getString(R.string.sign_anonymous_long_text),
            context.getString(R.string.sign_anonymous_clickable_text)
        ) {
            onAnonymouslySignClickCallback?.invoke()
        }

    }

}