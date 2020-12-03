package com.sabidos.data.remote.model

data class ErrorResponse(val code: Int, val message: String)
data class AuthErrorResponse(val code: String, val message: String?) {
    companion object {
        const val ERROR_CREDENTIAL_ALREADY_IN_USE = "ERROR_CREDENTIAL_ALREADY_IN_USE"
    }
}