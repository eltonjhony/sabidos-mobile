package com.sabidos.data.local.preferences

import android.content.Context
import com.sabidos.infrastructure.helpers.PrefsHelper

class UserProfilePhotoPrefsHelper(context: Context) : PrefsHelper(context) {

    override fun providePreferencesString(): String = "com.sabidos.profile.photo.helper"

    fun putPhoto(imagePath: String) =
        putString(USER_PROFILE_PHOTO_URI_PARAM, imagePath)

    fun getPhotoPath(): String? {
        val imagePath = getString(USER_PROFILE_PHOTO_URI_PARAM, "")
        return if (imagePath.isNotBlank()) imagePath else null
    }

    fun putOrientation(orientation: Int) =
        putInt(USER_PROFILE_PHOTO_ORIENTATION_PARAM, orientation)

    fun getOrientation(): Int =
        getInt(USER_PROFILE_PHOTO_ORIENTATION_PARAM, 0)

    companion object {
        private const val USER_PROFILE_PHOTO_URI_PARAM = "USER_PROFILE_PHOTO_URI_PARAM"
        private const val USER_PROFILE_PHOTO_ORIENTATION_PARAM =
            "USER_PROFILE_PHOTO_ORIENTATION_PARAM"
    }

}