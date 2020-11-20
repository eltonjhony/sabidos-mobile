package com.sabidos.data.local.preferences

import android.content.Context
import com.sabidos.infrastructure.helpers.PrefsHelper

class RoundPrefsHelper(context: Context) : PrefsHelper(context) {

    override fun providePreferencesString(): String = "com.sabidos.round.helper"

    fun didFinishRound() {
        putInt(ROUND_PARAM, getRound() + 1)
    }

    fun getRound(): Int = getInt(ROUND_PARAM, 1)

    companion object {
        private const val ROUND_PARAM = "ROUND_PARAM"
    }

}