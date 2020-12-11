package com.sabidos.infrastructure

import com.sabidos.data.remote.model.AuthErrorResponse
import com.sabidos.data.remote.model.ErrorResponse

open class Resource<out T> constructor(
    val state: ResourceState,
    val data: T? = null,
    val error: Error? = null,
    val errorResponse: ErrorResponse? = null,
    val authErrorResponse: AuthErrorResponse? = null
) {

    var hasBeenHandled = false
        private set

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): Resource<T>? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            this
        }
    }

}