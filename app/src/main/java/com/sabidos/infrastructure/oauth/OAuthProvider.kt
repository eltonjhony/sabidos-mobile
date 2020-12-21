package com.sabidos.infrastructure.oauth

import com.sabidos.domain.User
import com.sabidos.infrastructure.ResultWrapper

interface OAuthProvider {

    fun isUserLogged(): ResultWrapper<Boolean>

    fun isSignInWithEmailLink(emailLink: String): ResultWrapper<Boolean>

    suspend fun getCurrentUser(): ResultWrapper<User>

    fun getAuthToken(
        forceRefresh: Boolean,
        callback: ((ResultWrapper<String?>) -> Unit)
    )

    fun signInAnonymously(callback: ((ResultWrapper<User>) -> Unit))

    fun signInWithEmailLink(email: String, callback: ((ResultWrapper<Boolean>) -> Unit))

    fun completeSignWithEmailLink(emailLink: String, callback: (ResultWrapper<User>) -> Unit)

    suspend fun logout(): ResultWrapper<Boolean>

    fun verifyPhoneNumber(phoneNumber: String, callback: (ResultWrapper<Boolean>) -> Unit)

    fun signInWithPhoneNumber(code: String, callback: (ResultWrapper<User>) -> Unit)

    fun clear()
}