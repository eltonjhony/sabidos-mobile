package com.sabidos.domain.interactor

import com.sabidos.infrastructure.ResultWrapper
import com.sabidos.infrastructure.logging.Logger
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class UseCase<T, in Params> : CoroutineScope where T : Any? {

    private val parentJob = SupervisorJob()
    private val mainDispatcher = Dispatchers.Main
    private val backgroundDispatcher = Dispatchers.IO

    override val coroutineContext: CoroutineContext
        get() = parentJob + mainDispatcher

    protected abstract suspend fun run(params: Params): ResultWrapper<T>

    operator fun invoke(params: Params, onResult: (ResultWrapper<T>) -> Unit) {
        launch(context = mainDispatcher) {
            runCatching {
                val result = withContext(backgroundDispatcher) {
                    run(params)
                }
                logErrors(result)
                onResult(result)
            }.onFailure {
                Logger.withTag(UseCase::class.java.simpleName).withCause(it)
                onResult(ResultWrapper.GenericError(Error(it.message)))
            }
        }
    }

    fun clear() {
        runCatching {
            if (!parentJob.isActive) {
                parentJob.cancel()
            }
        }.onFailure {
            Logger.withTag(UseCase::class.java.simpleName).withCause(it)
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