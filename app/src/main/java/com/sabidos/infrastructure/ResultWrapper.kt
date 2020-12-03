package com.sabidos.infrastructure

import com.sabidos.data.remote.model.AuthErrorResponse
import com.sabidos.data.remote.model.ErrorResponse

sealed class ResultWrapper<out T> {
    data class Success<out T>(val data: T): ResultWrapper<T>()
    data class ApiError(val errorResponse: ErrorResponse? = null): ResultWrapper<Nothing>()
    data class AuthError(val errorResponse: AuthErrorResponse? = null): ResultWrapper<Nothing>()
    data class GenericError(val error: Error? = null): ResultWrapper<Nothing>()
    object NetworkError: ResultWrapper<Nothing>()
    object DataNotFoundError: ResultWrapper<Nothing>()
}