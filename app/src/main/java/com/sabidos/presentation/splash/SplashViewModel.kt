package com.sabidos.presentation.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabidos.data.local.cache.AccountCache
import com.sabidos.domain.Account
import com.sabidos.domain.User
import com.sabidos.domain.interactor.IsUserLoggedUseCase
import com.sabidos.domain.interactor.LoadInitialDataUseCase
import com.sabidos.domain.interactor.None
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResultWrapper
import com.sabidos.infrastructure.ResultWrapper.*
import com.sabidos.infrastructure.extensions.*
import com.sabidos.infrastructure.logging.Logger
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashViewModel(
    private val loadInitialDataUseCase: LoadInitialDataUseCase,
    private val isUserLoggedUseCase: IsUserLoggedUseCase,
    private val accountCache: AccountCache
) : ViewModel() {

    private val splashMinimalTime = 3000

    val userResource = MutableLiveData<Resource<User>>()
    val accountResource = MutableLiveData<Resource<Account>>()

    fun setupInitialSession() {
        accountResource.loading()

        // clear account caching to get updated data
        accountCache.invalidate()

        val startTime = System.currentTimeMillis()

        viewModelScope.launch {
            isUserLoggedUseCase(None()) {
                when (it) {
                    is Success -> {
                        if (it.data) {
                            viewModelScope.launch {
                                loadInitialDataUseCase(None()) { account ->
                                    delaySplashIfNeeded(startTime) {
                                        handleResult(account)
                                    }
                                }
                            }
                        } else {
                            delaySplashIfNeeded(startTime) {
                                userResource.setDataNotFound()
                            }
                        }
                    }
                    else -> {
                        delaySplashIfNeeded(startTime) {
                            userResource.setGenericFailure()
                        }
                    }
                }
            }
        }

    }

    private fun delaySplashIfNeeded(startTime: Long, callback: () -> Unit) {
        runCatching {
            val elapsedTime =
                splashMinimalTime - (System.currentTimeMillis() - startTime)
            if (elapsedTime > 0) {
                GlobalScope.launch {
                    withContext(Default) { delay(elapsedTime) }
                    callback.invoke()
                }
            } else {
                callback.invoke()
            }
        }.onFailure { ex ->
            Logger.withTag(SplashViewModel::class.java.simpleName).withCause(ex)
            callback.invoke()
        }
    }

    private fun handleResult(it: ResultWrapper<Account?>?) {
        it?.let {
            when (it) {
                is Success -> accountResource.setSuccess(it.data)
                is NetworkError -> accountResource.setNetworkFailure()
                is DataNotFoundError -> accountResource.setDataNotFound()
                else -> accountResource.setGenericFailure()
            }
        } ?: accountResource.setDataNotFound()
    }

}