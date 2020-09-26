package com.sabidos.presentation.ranking

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

class RankingViewModel(
    private val getWeeklyRankingUseCase: GetWeeklyRankingUseCase
) : ViewModel() {

    val rankingResource = MutableLiveData<Resource<RankingWrapper>>()

    fun loadWeeklyRanking(endDate: String) {
        rankingResource.loading()
        getWeeklyRankingUseCase(Params(endDate = endDate)) {
            when (it) {
                is Success -> rankingResource.setSuccess(it.data)
                is NetworkError -> rankingResource.setNetworkFailure()
                else -> rankingResource.setGenericFailure()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        getWeeklyRankingUseCase.clear()
    }

}