package com.sabidos.presentation.profile

import com.sabidos.domain.interactor.GetCurrentAccountUseCase
import com.sabidos.presentation.common.AccountViewModel

class ProfileViewModel(
    getCurrentAccountUseCase: GetCurrentAccountUseCase
) : AccountViewModel(getCurrentAccountUseCase)