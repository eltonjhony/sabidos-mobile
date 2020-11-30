package com.sabidos.infrastructure

import com.sabidos.BuildConfig

class Constants {

    interface Animation {
        companion object {
            const val MAIL_SENT_ANIMATION_PATH = "mail_sent_animation.json"
            const val CORRECT_ANIMATION_JSON = "correct_animation.json"
            const val INCORRECT_ANIMATION_JSON = "incorrect_animation.json"
            const val TIME_OVER_ANIMATION_JSON = "time_over_animation.json"
            const val PRIMARY_LOADING_ANIMATION_JSON = "primary_loading_animation.json"
            const val SECONDARY_LOADING_ANIMATION_JSON = "secondary_loading_animation.json"
        }
    }

    interface Configs {
        companion object {
            const val APPLICATION_ID = BuildConfig.APPLICATION_ID
            const val SABIDOS_MAGIC_LINK_URL = BuildConfig.SABIDOS_MAGIC_LINK_URL
            const val SDK_MIN_VERSION = BuildConfig.MIN_SDK_VERSION.toString()
        }
    }

}