package com.sabidos.presentation.onboarding

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabidos.domain.interactor.*
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResultWrapper.NetworkError
import com.sabidos.infrastructure.ResultWrapper.Success
import com.sabidos.infrastructure.extensions.*
import com.sabidos.infrastructure.helpers.PhoneNumberHelper
import kotlinx.coroutines.launch

class LoginViewModel(
    private val createAnonymousAccountUseCase: CreateAnonymousAccountUseCase,
    private val signInAnonymouslyUseCase: SignInAnonymouslyUseCase,
    private val verifyPhoneNumberUseCase: VerifyPhoneNumberUseCase,
    private val signInWithEmailLinkUseCase: SignInWithEmailLinkUseCase,
    private val phoneNumberHelper: PhoneNumberHelper
) : ViewModel() {

    val resource = MutableLiveData<Resource<Void>>()
    val signInAnonymouslyResource = MutableLiveData<Resource<Void>>()
    val signInWithEmailLinkResource = MutableLiveData<Resource<Void>>()
    val verifyPhoneNumberResource = MutableLiveData<Resource<Void>>()

    fun signInAnonymously() {
        signInAnonymouslyResource.loading()

        viewModelScope.launch {
            signInAnonymouslyUseCase(None()) {
                when (it) {
                    is Success -> createAnonymousAccount()
                    is NetworkError -> signInAnonymouslyResource.setNetworkFailure()
                    else -> signInAnonymouslyResource.setGenericFailure()
                }
            }
        }
    }

    fun signInWithEmailLink(email: String) {

        if (isMailNotValid(email)) {
            signInWithEmailLinkResource.setValidationFailure()
            return
        }

        signInWithEmailLinkResource.loading()

        viewModelScope.launch {
            signInWithEmailLinkUseCase(SignInWithEmailLinkUseCase.Params(email)) {
                when (it) {
                    is Success -> signInWithEmailLinkResource.setSuccess()
                    is NetworkError -> signInWithEmailLinkResource.setNetworkFailure()
                    else -> signInAnonymouslyResource.setGenericFailure()
                }
            }
        }

    }

    fun verifyPhoneNumber(phoneNumber: String) {

        if (!phoneNumberHelper.isPhoneNumberValid(phoneNumber)) {
            verifyPhoneNumberResource.setValidationFailure()
            return
        }

        verifyPhoneNumberResource.loading()

        viewModelScope.launch {
            verifyPhoneNumberUseCase(VerifyPhoneNumberUseCase.Params(phoneNumber)) {
                when (it) {
                    is Success -> verifyPhoneNumberResource.setSuccess()
                    is NetworkError -> verifyPhoneNumberResource.setNetworkFailure()
                    else -> verifyPhoneNumberResource.setGenericFailure()
                }
            }
        }

    }

    fun getFormattedPhoneNumber(phone: String): String =
        phoneNumberHelper.getFormattedPhoneNumber(phone)

    private fun createAnonymousAccount() {
        viewModelScope.launch {
            createAnonymousAccountUseCase(None()) {
                when (it) {
                    is Success -> signInAnonymouslyResource.setSuccess()
                    is NetworkError -> signInAnonymouslyResource.setNetworkFailure()
                    else -> signInAnonymouslyResource.setGenericFailure()
                }
            }
        }
    }

    private fun isMailNotValid(email: String): Boolean =
        email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()
}