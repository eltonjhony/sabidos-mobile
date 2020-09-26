package com.sabidos.presentation.profile.views

import android.content.Context
import android.util.AttributeSet
import com.sabidos.R
import com.sabidos.infrastructure.extensions.hide
import com.sabidos.infrastructure.extensions.show
import com.sabidos.presentation.components.BaseComponent
import kotlinx.android.synthetic.main.sabidos_user_managment_component.view.*

class UserManagmtComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_user_managment_component,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    var onLinkAccountClickListener: (() -> Unit)? = null
        set(value) {
            field = value
            setupLinkAccountButton()
        }

    var onLogoutClickLister: (() -> Unit)? = null
        set(value) {
            field = value
            setupLogoutButton()
        }

    override fun setupComponent() {
        super.setupComponent()
        linkAccountComponent.setText(context.getString(R.string.authenticate_account_label))
        linkAccountComponent.hide()

        logoutComponent.setText(context.getString(R.string.exit_label))
        logoutComponent.hide()
    }

    fun setup(isAnonymous: Boolean): Any = if (isAnonymous) {
        linkAccountComponent.show()
    } else {
        logoutComponent.show()
    }

    private fun setupLogoutButton() {
        logoutComponent.onClickListener { onLogoutClickLister?.invoke() }
    }

    private fun setupLinkAccountButton() {
        linkAccountComponent.onClickListener { onLinkAccountClickListener?.invoke() }
    }

}