package com.sabidos.presentation.onboarding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabidos.domain.interactor.ValidateAccountUseCase
import com.sabidos.domain.interactor.ValidateAccountUseCase.ValidateAccountParam
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResultWrapper.*
import com.sabidos.infrastructure.extensions.*
import kotlinx.coroutines.launch

class PersonalInfoViewModel(
    private val validateAccountUseCase: ValidateAccountUseCase
) : ViewModel() {

    val validateAccountResource = MutableLiveData<Resource<Void>>()

    fun validateAccount(nickname: String) {
        validateAccountResource.loading()

        viewModelScope.launch {
            validateAccountUseCase(ValidateAccountParam(nickname)) {
                when (it) {
                    is Success -> validateAccountResource.setSuccess()
                    is NetworkError -> validateAccountResource.setNetworkFailure()
                    is ApiError -> validateAccountResource.setApiFailure(it.errorResponse)
                    else -> validateAccountResource.setGenericFailure()
                }
            }
        }
    }

}