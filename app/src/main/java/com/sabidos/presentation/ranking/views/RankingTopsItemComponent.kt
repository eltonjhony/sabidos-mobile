package com.sabidos.presentation.ranking.views

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.ColorRes
import com.sabidos.R
import com.sabidos.domain.UserAvatar
import com.sabidos.infrastructure.extensions.color
import com.sabidos.infrastructure.extensions.hide
import com.sabidos.infrastructure.extensions.show
import com.sabidos.presentation.components.BaseComponent
import kotlinx.android.synthetic.main.sabidos_ranking_tops_item.view.*

class RankingTopsItemComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_ranking_tops_item,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    var totalHits: Int = 0
        set(value) {
            field = value
            setupTotalHits()
        }

    var stars: Int = 0
        set(value) {
            field = value
            setupStarComponent()
        }

    var level: Int = 0
        set(value) {
            field = value
            setupLevelComponent()
        }

    var nickname: String = ""
        set(value) {
            field = value
            setupNickname()
        }

    var firstName: String = ""
        set(value) {
            field = value
            setupFirstName()
        }

    var userAvatar: UserAvatar? = null
        set(value) {
            field = value
            setupPlayerPhoto()
        }

    var position: Int = 0
        set(value) {
            field = value
            setupPosition()
        }

    @ColorRes
    var textColor: Int? = null
        set(value) {
            field = value
            setupTextColor()
        }

    override fun setupComponent() {
        super.setupComponent()
        levelStarsComponent.hide()
        levelStatusComponent.hide()
    }

    private fun setupTextColor() {
        textColor?.let {
            hitsValueLabel.setTextColor(context.color(it))
            nicknameLabel.setTextColor(context.color(it))
            firstNameLabel.setTextColor(context.color(it))
            positionIconLabel.setTextColor(context.color(it))
        }
    }

    private fun setupTotalHits() {
        hitsValueLabel.text = "$totalHits"
    }

    private fun setupStarComponent() {
        levelStarsComponent.show()
        levelStarsComponent.stars = stars
    }

    private fun setupLevelComponent() {
        levelStatusComponent.show()
        levelStatusComponent.level = level
    }

    private fun setupNickname() {
        nicknameLabel.text = nickname
    }

    private fun setupFirstName() {
        firstNameLabel.text = firstName
    }

    private fun setupPlayerPhoto() {
        playerImageView.userAvatar = userAvatar
    }

    private fun setupPosition() {
        positionIconLabel.text = "$position º"
        when (position) {
            1 -> {
                positionIconView.show()
                positionIconLabel.hide()
            }
            2, 3 -> {
                positionIconView.hide()
                positionIconLabel.show()
            }
        }
    }
}