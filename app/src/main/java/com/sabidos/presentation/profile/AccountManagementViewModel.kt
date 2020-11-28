package com.sabidos.presentation.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabidos.domain.User
import com.sabidos.domain.interactor.GetCurrentUserUseCase
import com.sabidos.domain.interactor.None
import com.sabidos.domain.interactor.SignOutUseCase
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResultWrapper
import com.sabidos.infrastructure.extensions.setGenericFailure
import com.sabidos.infrastructure.extensions.setSuccess
import kotlinx.coroutines.launch

class AccountManagementViewModel(
    private val signOutUseCase: SignOutUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    val signOutResource = MutableLiveData<Resource<Boolean?>>()
    val currentUserResource = MutableLiveData<Resource<User?>>()

    fun getCurrentUser() {
        viewModelScope.launch {
            getCurrentUserUseCase(None()) {
                when (it) {
                    is ResultWrapper.Success -> currentUserResource.setSuccess(it.data)
                    else -> currentUserResource.setGenericFailure()
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
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

}