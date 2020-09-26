package com.sabidos.data.remote

import com.sabidos.infrastructure.ResultWrapper
import retrofit2.HttpException

object ErrorHandling {

    fun parse(throwable: Throwable): ResultWrapper<Nothing> {
        return when (throwable) {
            is HttpException -> {
                when (throwable.code()) {
                    404 -> ResultWrapper.DataNotFoundError
                    else -> ResultWrapper.GenericError(Error(throwable.message))
                }
            }
            else -> {
                ResultWrapper.GenericError(Error(throwable.message))
            }
        }
    }

}