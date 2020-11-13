package com.sabidos.domain.interactor

import com.sabidos.infrastructure.ResultWrapper
import com.sabidos.infrastructure.logging.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class UseCase<T, in Params> where T : Any? {

    protected abstract suspend fun run(params: Params): ResultWrapper<T>

    suspend operator fun invoke(params: Params, onResult: (ResultWrapper<T>) -> Unit) {
        runCatching {
            val result = withContext(Dispatchers.IO) {
                run(params)
            }
            logErrors(result)
            onResult(result)
        }.onFailure {
            Logger.withTag(UseCase::class.java.simpleName).withCause(it)
            onResult(ResultWrapper.GenericError(Error(it.message)))
        }
    }

    private fun logErrors(result: ResultWrapper<T>) {
        runCatching {
            when (result) {
                is ResultWrapper.GenericError -> {
                    Logger.withTag(UseCase::class.java.simpleName).withCause(result.error)
                }
                is ResultWrapper.NetworkError -> {
                    Logger.withTag(UseCase::class.java.simpleName)
                        .withCause(Throwable("Network not Available at the moment"))
                }
                is ResultWrapper.ApiError -> {
                    Logger.withTag(UseCase::class.java.simpleName)
                        .withCause(Throwable(result.errorResponse.toString()))
                }
            }
        }
    }

}

class None