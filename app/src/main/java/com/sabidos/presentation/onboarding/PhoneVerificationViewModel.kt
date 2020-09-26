package com.sabidos.presentation.onboarding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sabidos.domain.interactor.SignInWithPhoneNumberUseCase
import com.sabidos.domain.interactor.VerifyPhoneNumberUseCase
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResultWrapper.NetworkError
import com.sabidos.infrastructure.ResultWrapper.Success
import com.sabidos.infrastructure.extensions.loading
import com.sabidos.infrastructure.extensions.setGenericFailure
import com.sabidos.infrastructure.extensions.setNetworkFailure
import com.sabidos.infrastructure.extensions.setSuccess
import com.sabidos.infrastructure.helpers.SignInPrefsHelper

class PhoneVerificationViewModel(
    private val signInWithPhoneNumberUseCase: SignInWithPhoneNumberUseCase,
    private val verifyPhoneNumberUseCase: VerifyPhoneNumberUseCase,
    private val signInPrefsHelper: SignInPrefsHelper
) : ViewModel() {

    val signInWithPhoneNumberResource = MutableLiveData<Resource<Void>>()
    val resendCodeResource = MutableLiveData<Resource<Void>>()

    fun signInWithPhoneNumber(code: String) {
        signInWithPhoneNumberResource.loading()

        signInWithPhoneNumberUseCase(SignInWithPhoneNumberUseCase.Params(code)) {
            when (it) {
                is Success -> signInWithPhoneNumberResource.setSuccess()
                is NetworkError -> signInWithPhoneNumberResource.setNetworkFailure()
                else -> signInWithPhoneNumberResource.setGenericFailure()
            }
        }
    }

    fun resendCode() {

        val phoneNumber = signInPrefsHelper.getPhoneNumber()

        phoneNumber?.let { phone ->
            resendCodeResource.loading()

            verifyPhoneNumberUseCase(VerifyPhoneNumberUseCase.Params(phone)) {
                when (it) {
                    is Success -> resendCodeResource.setSuccess()
                    is NetworkError -> resendCodeResource.setNetworkFailure()
                    else -> resendCodeResource.setGenericFailure()
                }
            }
        } ?: resendCodeResource.setGenericFailure()

    }

    fun getMaskedPhone(): String? {
        val phoneNumber = signInPrefsHelper.getPhoneNumber()
        return phoneNumber?.replaceRange(0, phoneNumber.length - 2, "** *******")
    }

    override fun onCleared() {
        super.onCleared()
        signInWithPhoneNumberUseCase.clear()
        verifyPhoneNumberUseCase.clear()
    }

}