package com.sabidos.presentation.splash

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabidos.domain.interactor.CompleteSignInWithEmailLinkUseCase
import com.sabidos.domain.interactor.IsSignInWithEmailLinkUseCase
import com.sabidos.infrastructure.ResultWrapper
import com.sabidos.infrastructure.ResultWrapper.*
import kotlinx.coroutines.launch
import com.sabidos.domain.interactor.CompleteSignInWithEmailLinkUseCase.Params as Params2
import com.sabidos.domain.interactor.IsSignInWithEmailLinkUseCase.Params as Params1

class DeepLinksViewModel(
    private val isSignInWithEmailLinkUseCase: IsSignInWithEmailLinkUseCase,
    private val completeSignInWithEmailLinkUseCase: CompleteSignInWithEmailLinkUseCase
): ViewModel() {

    fun handle(data: Uri, callback: (ResultWrapper<Boolean>) -> Unit) {

        viewModelScope.launch {
            isSignInWithEmailLinkUseCase(Params1(data.toString())) {

                when (it) {
                    is Success -> completeSignWithMagicLink(data, callback)
                    else -> callback(Success(true))
                }

            }
        }

    }

    private fun completeSignWithMagicLink(
        data: Uri,
        callback: (ResultWrapper<Boolean>) -> Unit
    ) {

        viewModelScope.launch {
            completeSignInWithEmailLinkUseCase(Params2(data.toString())) {
                when (it) {
                    is Success -> callback(Success(true))
                    is AuthError -> callback(it)
                    else -> callback(GenericError())
                }

            }
        }
    }

}