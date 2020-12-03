package com.sabidos.presentation.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabidos.data.local.preferences.UserProfilePhotoPrefsHelper
import com.sabidos.domain.Account
import com.sabidos.domain.interactor.GetCurrentAccountUseCase
import com.sabidos.domain.interactor.None
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResultWrapper.Success
import com.sabidos.infrastructure.extensions.loading
import com.sabidos.infrastructure.extensions.setSuccess
import kotlinx.coroutines.launch

open class AccountViewModel(
    private val getCurrentAccountUseCase: GetCurrentAccountUseCase,
    private val profilePhotoPrefsHelper: UserProfilePhotoPrefsHelper
) : ViewModel() {

    val accountResource = MutableLiveData<Resource<Account?>>()

    fun getCurrentAccount() {
        accountResource.loading()
        viewModelScope.launch {
            getCurrentAccountUseCase(None()) {
                when (it) {
                    is Success -> handleSuccess(it.data)
                }
            }
        }
    }

    fun saveUserProfilePhoto(imagePath: String?, orientation: Int) {
        imagePath?.let {
            profilePhotoPrefsHelper.putPhoto(it)
            profilePhotoPrefsHelper.putOrientation(orientation)
        }
    }

    private fun handleSuccess(account: Account?) {
        account?.avatar?.localUserPhoto = profilePhotoPrefsHelper.getPhotoPath()
        account?.avatar?.orientation = profilePhotoPrefsHelper.getOrientation()
        accountResource.setSuccess(account)
    }

}