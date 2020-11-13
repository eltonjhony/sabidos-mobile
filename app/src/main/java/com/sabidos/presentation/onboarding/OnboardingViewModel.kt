package com.sabidos.presentation.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabidos.domain.Account
import com.sabidos.domain.User
import com.sabidos.domain.UserAvatar
import com.sabidos.domain.interactor.CreateAccountUseCase
import com.sabidos.domain.interactor.CreateAccountUseCase.Params
import com.sabidos.domain.interactor.GetCurrentUserUseCase
import com.sabidos.domain.interactor.None
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResultWrapper.*
import com.sabidos.infrastructure.extensions.*
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val createAccountUseCase: CreateAccountUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    companion object {
        const val SIGN_IN_STEP = 0
        const val PERSONAL_INFO_STEP = 1
        const val USER_AVATAR_STEP = 2
        const val STEPS_COMPLETED = 3

        // phone sign steps
        const val PHONE_VERIFICATION_STEP = 4
    }

    private val _currentStep = MutableLiveData<Int>()

    val currentStep: LiveData<Int> = _currentStep

    val completeProfileResource = MutableLiveData<Resource<Void>>()

    var name: String = ""
        private set
    var nickname: String = ""
        private set
    
    private var userAvatar = UserAvatar(1, "")

    var isBackPressed: Boolean = false
        private set

    var isAnonymousMigration = false

    fun defineStep(step: Int) {
        _currentStep.value = step
    }

    fun nextStep() {
        isBackPressed = false
        _currentStep.value = _currentStep.value?.plus(1)
    }

    fun forceFinishStep() {
        isBackPressed = false
        _currentStep.value = STEPS_COMPLETED
    }

    fun goBackStep() {
        isBackPressed = true
        _currentStep.value = _currentStep.value?.minus(1)
    }

    fun canGoBack(): Boolean = _currentStep.value == USER_AVATAR_STEP

    fun setupPersonalInfo(name: String, nickname: String) {
        this.name = name
        this.nickname = nickname
    }

    fun setupUserAvatar(userAvatar: UserAvatar) {
        this.userAvatar = userAvatar
    }

    fun completeUserProfile() {
        completeProfileResource.loading()

        viewModelScope.launch {
            getCurrentUserUseCase(None()) { userResource ->
                when (userResource) {
                    is Success -> {
                        userResource.data?.let { user ->
                            createAccountFor(user)
                        }
                    }
                    is NetworkError -> completeProfileResource.setNetworkFailure()
                    is ApiError -> completeProfileResource.setApiFailure(userResource.errorResponse)
                    else -> completeProfileResource.setGenericFailure()
                }
            }
        }

    }

    private fun createAccountFor(user: User) {
        val account = Account(name, nickname, userAvatar)

        viewModelScope.launch {
            createAccountUseCase(Params(account, user)) {
                when (it) {
                    is Success -> completeProfileResource.setSuccess()
                    is NetworkError -> completeProfileResource.setNetworkFailure()
                    is ApiError -> completeProfileResource.setApiFailure(it.errorResponse)
                    else -> completeProfileResource.setGenericFailure()
                }
            }
        }
    }

}