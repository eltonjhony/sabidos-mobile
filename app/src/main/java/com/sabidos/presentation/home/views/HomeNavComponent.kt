package com.sabidos.presentation.home.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.sabidos.R
import com.sabidos.domain.UserAvatar
import kotlinx.android.synthetic.main.sabidos_home_nav_component.view.*

class HomeNavComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : Toolbar(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.sabidos_home_nav_component, this)
    }

    var userAvatar: UserAvatar? = null
        set(value) {
            field = value
            avatarComponent.userAvatar = value
        }

    var username: String? = null
        set(value) {
            field = value
            setupUserName()
        }

    private fun setupUserName() {
        welcomeText.text = context.getString(R.string.hello_label, username)
        readyTextView.text = context.getString(R.string.motivational_label)
    }

}