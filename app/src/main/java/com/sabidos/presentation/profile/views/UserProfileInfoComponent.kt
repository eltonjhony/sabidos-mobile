package com.sabidos.presentation.profile.views

import android.content.Context
import android.util.AttributeSet
import com.sabidos.R
import com.sabidos.domain.Account
import com.sabidos.infrastructure.extensions.hide
import com.sabidos.infrastructure.extensions.show
import com.sabidos.presentation.components.BaseComponent
import kotlinx.android.synthetic.main.sabidos_user_profile_info_component.view.*

class UserProfileInfoComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_user_profile_info_component,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    fun setup(account: Account) {
        contentWrapper.show()
        loadingView.stopAnimation()
        loadingView.hide()
        playerNameTextView.text = account.name
        levelStatusComponent.level = account.reputation?.level
        levelStarsComponent.stars = account.reputation?.stars
        answeredValue.text = "${account.totalAnswered}"
        hitsValue.text = "${account.totalHits}"
    }

    fun startLoading() {
        loadingView.startAnimation()
        contentWrapper.hide()
    }

    fun showError() {
        contentWrapper.hide()
        loadingView.hide()
    }

}