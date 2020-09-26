package com.sabidos.presentation.profile.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.sabidos.R
import com.sabidos.presentation.components.BaseComponent
import kotlinx.android.synthetic.main.sabidos_user_managmt_buttom_component.view.*

class UserManagmtButtonComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_user_managmt_buttom_component,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    fun onClickListener(callback: (View) -> Unit) {
        logoutButton.setOnClickListener { callback(it) }
    }

    fun setText(text: String) {
        logoutButton.text = text
    }

}