package com.sabidos.presentation.profile

import com.sabidos.data.local.preferences.UserProfilePhotoPrefsHelper
import com.sabidos.domain.interactor.GetCurrentAccountUseCase
import com.sabidos.presentation.common.AccountViewModel

class ProfileViewModel(
    getCurrentAccountUseCase: GetCurrentAccountUseCase,
    profilePhotoPrefsHelper: UserProfilePhotoPrefsHelper
) : AccountViewModel(getCurrentAccountUseCase, profilePhotoPrefsHelper)