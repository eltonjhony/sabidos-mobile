package com.sabidos.domain.interactor

import com.sabidos.data.local.AppDatabase
import com.sabidos.data.local.cache.CacheHandler
import com.sabidos.domain.User
import com.sabidos.infrastructure.ResultWrapper
import com.sabidos.infrastructure.ResultWrapper.Success
import com.sabidos.infrastructure.oauth.OAuthProvider
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GetCurrentUserUseCase(private val oauthProvider: OAuthProvider) : UseCase<User?, None>() {
    override suspend fun run(params: None): ResultWrapper<User?> = oauthProvider.getCurrentUser()
}

class SignInAnonymouslyUseCase(private val oauthProvider: OAuthProvider) : UseCase<User?, None>() {

    override suspend fun run(params: None): ResultWrapper<User?> = suspendCoroutine { cont ->
        oauthProvider.signInAnonymously {
            cont.resume(it)
        }
    }

}

class SignInWithEmailLinkUseCase(private val oauthProvider: OAuthProvider) :
    UseCase<Boolean?, SignInWithEmailLinkUseCase.Params>() {

    override suspend fun run(params: Params): ResultWrapper<Boolean?> = suspendCoroutine { cont ->
        oauthProvider.signInWithEmailLink(params.email) {
            cont.resume(it)
        }
    }

    data class Params(val email: String)

}

class VerifyPhoneNumberUseCase(private val oauthProvider: OAuthProvider) :
    UseCase<Boolean?, VerifyPhoneNumberUseCase.Params>() {

    override suspend fun run(params: Params): ResultWrapper<Boolean?> = suspendCoroutine { cont ->
        oauthProvider.verifyPhoneNumber(params.phoneNumber) {
            oauthProvider.clear()
            cont.resume(it)
        }
    }

    data class Params(val phoneNumber: String)

}

class SignInWithPhoneNumberUseCase(private val oauthProvider: OAuthProvider) :
    UseCase<Boolean?, SignInWithPhoneNumberUseCase.Params>() {

    override suspend fun run(params: Params): ResultWrapper<Boolean?> = suspendCoroutine { cont ->
        oauthProvider.signInWithPhoneNumber(params.code) {
            cont.resume(it)
        }
    }

    data class Params(val code: String)

}

class CompleteSignInWithEmailLinkUseCase(private val oauthProvider: OAuthProvider) :
    UseCase<User?, CompleteSignInWithEmailLinkUseCase.Params>() {

    override suspend fun run(params: Params): ResultWrapper<User?> = suspendCoroutine { cont ->
        oauthProvider.completeSignWithEmailLink(params.emailLink) {
            cont.resume(it)
        }
    }

    data class Params(val emailLink: String)
}

class IsUserLoggedUseCase(private val oauthProvider: OAuthProvider) : UseCase<Boolean, None>() {
    override suspend fun run(params: None): ResultWrapper<Boolean> = oauthProvider.isUserLogged()
}

class IsSignInWithEmailLinkUseCase(private val oauthProvider: OAuthProvider) :
    UseCase<Boolean, IsSignInWithEmailLinkUseCase.Params>() {
    override suspend fun run(params: Params): ResultWrapper<Boolean> =
        oauthProvider.isSignInWithEmailLink(params.emailLink)

    data class Params(val emailLink: String)
}

class SignOutUseCase(
    private val oauthProvider: OAuthProvider,
    private val appDatabase: AppDatabase,
    private val cacheHandler: CacheHandler
) : UseCase<Boolean, None>() {
    override suspend fun run(params: None): ResultWrapper<Boolean> {
        return when (val logoutCommand = oauthProvider.logout()) {
            is Success -> {
                appDatabase.clearAllData()
                cacheHandler.invalidateAll()
                logoutCommand
            }
            else -> logoutCommand
        }

    }
}