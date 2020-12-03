package com.sabidos.presentation.ranking

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabidos.data.local.preferences.UserProfilePhotoPrefsHelper
import com.sabidos.domain.RankingWrapper
import com.sabidos.domain.interactor.GetWeeklyRankingUseCase
import com.sabidos.domain.interactor.GetWeeklyRankingUseCase.Params
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResultWrapper.NetworkError
import com.sabidos.infrastructure.ResultWrapper.Success
import com.sabidos.infrastructure.extensions.loading
import com.sabidos.infrastructure.extensions.setGenericFailure
import com.sabidos.infrastructure.extensions.setNetworkFailure
import com.sabidos.infrastructure.extensions.setSuccess
import kotlinx.coroutines.launch

class RankingViewModel(
    private val getWeeklyRankingUseCase: GetWeeklyRankingUseCase,
    private val userProfilePhotoPrefsHelper: UserProfilePhotoPrefsHelper
) : ViewModel() {

    val rankingResource = MutableLiveData<Resource<RankingWrapper>>()

    fun loadWeeklyRanking(endDate: String) {
        rankingResource.loading()
        viewModelScope.launch {
            getWeeklyRankingUseCase(Params(endDate = endDate)) {
                when (it) {
                    is Success -> handleSuccess(it.data)
                    is NetworkError -> rankingResource.setNetworkFailure()
                    else -> rankingResource.setGenericFailure()
                }
            }
        }
    }

    private fun handleSuccess(rankingWrapper: RankingWrapper) {
        userProfilePhotoPrefsHelper.getPhotoPath()?.let {
            rankingWrapper.setProfilePhotoPath(it, userProfilePhotoPrefsHelper.getOrientation())
        }
        rankingResource.setSuccess(rankingWrapper)
    }

}