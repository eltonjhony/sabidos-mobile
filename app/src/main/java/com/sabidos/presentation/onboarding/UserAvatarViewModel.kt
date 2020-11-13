package com.sabidos.presentation.onboarding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabidos.domain.UserAvatar
import com.sabidos.domain.interactor.GetAllAvatarsUseCase
import com.sabidos.domain.interactor.None
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResultWrapper.NetworkError
import com.sabidos.infrastructure.ResultWrapper.Success
import com.sabidos.infrastructure.extensions.loading
import com.sabidos.infrastructure.extensions.setGenericFailure
import com.sabidos.infrastructure.extensions.setNetworkFailure
import com.sabidos.infrastructure.extensions.setSuccess
import kotlinx.coroutines.launch

class UserAvatarViewModel(
    private val getAllAvatarsUseCase: GetAllAvatarsUseCase
) : ViewModel() {

    val avatarsResource = MutableLiveData<Resource<List<UserAvatar>>>()

    fun getAllAvatars() {
        avatarsResource.loading()

        viewModelScope.launch {
            getAllAvatarsUseCase(None()) {
                when (it) {
                    is Success -> avatarsResource.setSuccess(it.data)
                    is NetworkError -> avatarsResource.setNetworkFailure()
                    else -> avatarsResource.setGenericFailure()
                }
            }
        }
    }

}