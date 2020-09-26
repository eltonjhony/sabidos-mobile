package com.sabidos.presentation.profile.views

import android.content.Context
import android.util.AttributeSet
import com.sabidos.R
import com.sabidos.domain.Account
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
        playerNameTextView.text = "${account.name} - ${account.nickname}"
        levelStatusComponent.level = account.reputation?.level
        levelStarsComponent.stars = account.reputation?.stars
        answeredValue.text = "${account.totalAnswered}"
        hitsValue.text = "${account.totalHits}"
    }

}