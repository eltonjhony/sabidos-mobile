package com.sabidos.data.remote

import com.sabidos.infrastructure.ResultWrapper
import com.sabidos.infrastructure.logging.Logger
import com.sabidos.infrastructure.oauth.OAuthProvider
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SecurityCloudApiFactory(
    private val oauthProvider: OAuthProvider
) : CloudApiFactory {

    override suspend fun <T> create(service: Class<T>): T = suspendCoroutine { cont ->

        oauthProvider.getAuthToken(true) {

            val token = when (it) {
                is ResultWrapper.Success -> it.data
                is ResultWrapper.GenericError -> {
                    Logger.withTag(SecurityCloudApiFactory::class.java.simpleName).withCause(it.error)
                    null
                }
                else -> null
            } ?: ""

            cont.resume(ServiceFactory.getRetrofit(token).create(service))
        }

    }

}