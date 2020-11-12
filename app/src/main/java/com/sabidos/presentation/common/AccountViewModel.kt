package com.sabidos.presentation.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sabidos.domain.Account
import com.sabidos.domain.interactor.GetCurrentAccountUseCase
import com.sabidos.domain.interactor.None
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResultWrapper.Success
import com.sabidos.infrastructure.extensions.setSuccess

open class AccountViewModel(
    private val getCurrentAccountUseCase: GetCurrentAccountUseCase
) : ViewModel() {

    val accountResource = MutableLiveData<Resource<Account?>>()

    fun getCurrentAccount() {
        getCurrentAccountUseCase(None()) {
            when (it) {
                is Success -> accountResource.setSuccess(it.data)
            }
        }
    }

}