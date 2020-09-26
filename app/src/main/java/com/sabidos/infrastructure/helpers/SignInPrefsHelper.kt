package com.sabidos.infrastructure.helpers

import android.content.Context

class SignInPrefsHelper(context: Context) : PrefsHelper(context) {
    override fun providePreferencesString(): String = "com.sabidos.sign.in.helper"

    fun putEmail(email: String) = putString(SIGN_EMAIL_ADDRESS_PARAM, email)
    fun getEmail(): String? = getString(SIGN_EMAIL_ADDRESS_PARAM)

    fun putVerificationId(verificationId: String) = putString(VERIFICATION_ID_PARAM, verificationId)
    fun getVerificationId(): String? = getString(VERIFICATION_ID_PARAM)

    fun putPhoneNumber(phoneNumber: String) = putString(SIGN_PHONE_NUMBER_PARAM, phoneNumber)
    fun getPhoneNumber(): String? = getString(SIGN_PHONE_NUMBER_PARAM)

    companion object {
        private const val SIGN_EMAIL_ADDRESS_PARAM = "SIGN_EMAIL_ADDRESS"
        private const val VERIFICATION_ID_PARAM = "VERIFICATION_ID"
        private const val SIGN_PHONE_NUMBER_PARAM = "SIGN_PHONE_NUMBER"
    }

}