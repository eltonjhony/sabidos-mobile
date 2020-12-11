package com.sabidos.data.remote

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sabidos.data.remote.model.ErrorResponse
import com.sabidos.infrastructure.ResultWrapper
import com.sabidos.infrastructure.logging.Logger
import retrofit2.HttpException

object ErrorHandling {

    fun parse(throwable: Throwable): ResultWrapper<Nothing> {
        return when (throwable) {
            is HttpException -> {
                when (throwable.code()) {
                    404 -> ResultWrapper.DataNotFoundError
                    400 -> parseApiError(throwable)
                    else -> ResultWrapper.GenericError(Error(throwable.message))
                }
            }
            else -> {
                ResultWrapper.GenericError(Error(throwable.message))
            }
        }
    }

    private fun parseApiError(throwable: HttpException): ResultWrapper<Nothing> =
        runCatching {
            val errorBody = throwable.response()?.errorBody()?.string() ?: ""
            val errorParser = Gson().fromJson<ErrorResponse>(errorBody)
            return ResultWrapper.ApiError(errorParser)
        }.getOrElse {
            Logger.withTag(ErrorHandling::class.java.simpleName).withCause(it)
            return ResultWrapper.GenericError(Error(throwable.message))
        }

}

inline fun <reified T> Gson.fromJson(json: String) = fromJson<T>(json, object: TypeToken<T>() {}.type)