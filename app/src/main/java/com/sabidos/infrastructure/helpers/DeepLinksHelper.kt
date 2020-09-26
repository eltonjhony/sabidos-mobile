package com.sabidos.infrastructure.helpers

import android.net.Uri
import com.sabidos.domain.interactor.CompleteSignInWithEmailLinkUseCase
import com.sabidos.domain.interactor.IsSignInWithEmailLinkUseCase
import com.sabidos.infrastructure.ResultWrapper
import com.sabidos.infrastructure.ResultWrapper.GenericError
import com.sabidos.infrastructure.ResultWrapper.Success
import com.sabidos.domain.interactor.CompleteSignInWithEmailLinkUseCase.Params as Params2
import com.sabidos.domain.interactor.IsSignInWithEmailLinkUseCase.Params as Params1

class DeepLinksHelper(
    private val isSignInWithEmailLinkUseCase: IsSignInWithEmailLinkUseCase,
    private val completeSignInWithEmailLinkUseCase: CompleteSignInWithEmailLinkUseCase
) {

    fun handle(data: Uri, callback: (ResultWrapper<Boolean>) -> Unit) {

        isSignInWithEmailLinkUseCase(Params1(data.toString())) {

            when (it) {
                is Success -> completeSignWithMagicLink(data, callback)
                else -> callback(Success(true))
            }

        }

    }

    private fun completeSignWithMagicLink(
        data: Uri,
        callback: (ResultWrapper<Boolean>) -> Unit
    ) {
        completeSignInWithEmailLinkUseCase(Params2(data.toString())) {
            when (it) {
                is Success -> callback(Success(true))
                else -> callback(GenericError())
            }

        }
    }

}