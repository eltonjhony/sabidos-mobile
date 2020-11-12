package com.sabidos.presentation.profile

import androidx.lifecycle.MutableLiveData
import com.sabidos.domain.User
import com.sabidos.domain.interactor.GetCurrentAccountUseCase
import com.sabidos.domain.interactor.GetCurrentUserUseCase
import com.sabidos.domain.interactor.None
import com.sabidos.domain.interactor.SignOutUseCase
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResultWrapper
import com.sabidos.infrastructure.extensions.setGenericFailure
import com.sabidos.infrastructure.extensions.setSuccess
import com.sabidos.presentation.common.AccountViewModel

class ProfileViewModel(
    getCurrentAccountUseCase: GetCurrentAccountUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val signOutUseCase: SignOutUseCase
) : AccountViewModel(getCurrentAccountUseCase) {

    val currentUserResource = MutableLiveData<Resource<User?>>()
    val signOutResource = MutableLiveData<Resource<Boolean?>>()

    fun getCurrentUser() {
        getCurrentUserUseCase(None()) {
            when (it) {
                is ResultWrapper.Success -> currentUserResource.setSuccess(it.data)
                else -> currentUserResource.setGenericFailure()
            }
        }
    }

    fun signOut() {
        signOutUseCase(None()) {
            when (it) {
                is ResultWrapper.Success -> {
                    signOutResource.setSuccess(it.data)
                }
                else -> signOutResource.setGenericFailure()
            }
        }
    }

}