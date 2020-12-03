package com.sabidos.presentation.components

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import com.sabidos.R
import com.sabidos.domain.UserAvatar
import com.sabidos.infrastructure.extensions.*
import kotlinx.android.synthetic.main.sabidos_avatar_component.view.*

class AvatarComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_avatar_component,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    var orientation: Int = 0

    var imagePath: String? = null
        set(value) {
            field = value
            setupPhotoWithImageUri()
        }

    var userAvatar: UserAvatar? = null
        set(value) {
            field = value
            setupPlayerPhoto()
        }

    @DrawableRes
    var drawable: Int? = null
        set(value) {
            field = value
            setupDrawableImage(value)
        }

    private fun setupDrawableImage(@DrawableRes resId: Int?) {
        resId?.let {
            avatarImage.setImageDrawable(
                context.drawable(
                    it
                )
            )
        }
    }

    private fun setupPlayerPhoto() {
        userAvatar?.localUserPhoto?.let {
            orientation = userAvatar?.orientation ?: 0
            imagePath = it
        } ?: avatarImage.load(userAvatar?.imageUrl)
    }

    private fun setupPhotoWithImageUri() {
        imagePath?.let {
            avatarImage.loadAsBitmap(it, orientation) { isSuccess ->
                if (isSuccess) {
                    avatarImage.circularImage()
                } else {
                    avatarImage.load(userAvatar?.imageUrl)
                }
            }
        }
    }

    fun loadWithProgress(url: String?) {
        avatarImage.loadShowingProgressBar(url)
    }

}
