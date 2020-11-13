package com.sabidos.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sabidos.domain.Timeline
import com.sabidos.domain.WeeklyHits
import com.sabidos.domain.interactor.GetCurrentAccountUseCase
import com.sabidos.domain.interactor.GetTimelineUseCase
import com.sabidos.domain.interactor.GetWeeklyHitsUseCase
import com.sabidos.domain.interactor.GetWeeklyHitsUseCase.Params
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResultWrapper.NetworkError
import com.sabidos.infrastructure.ResultWrapper.Success
import com.sabidos.infrastructure.extensions.loading
import com.sabidos.infrastructure.extensions.setGenericFailure
import com.sabidos.infrastructure.extensions.setNetworkFailure
import com.sabidos.infrastructure.extensions.setSuccess
import com.sabidos.presentation.common.AccountViewModel
import kotlinx.coroutines.launch
import com.sabidos.domain.interactor.GetTimelineUseCase.Params as GetTimelineParams

class HomeViewModel(
    getCurrentAccountUseCase: GetCurrentAccountUseCase,
    private val getWeeklyHitsUseCase: GetWeeklyHitsUseCase,
    private val getTimelineUseCase: GetTimelineUseCase
) : AccountViewModel(getCurrentAccountUseCase) {

    val weeklyHitsResource = MutableLiveData<Resource<List<WeeklyHits>>>()
    val timelineResource = MutableLiveData<Resource<List<Timeline>>>()

    fun getWeeklyHitsFor(endDate: String) {
        weeklyHitsResource.loading()
        viewModelScope.launch {
            getWeeklyHitsUseCase(Params(endDate = endDate)) {
                when (it) {
                    is Success -> weeklyHitsResource.setSuccess(it.data)
                    is NetworkError -> weeklyHitsResource.setNetworkFailure()
                    else -> weeklyHitsResource.setGenericFailure()
                }
            }
        }
    }

    fun loadTimeline(page: Int = 1) {
        timelineResource.loading()
        viewModelScope.launch {
            getTimelineUseCase(GetTimelineParams(page = page)) {
                when (it) {
                    is Success -> timelineResource.setSuccess(it.data)
                    is NetworkError -> timelineResource.setNetworkFailure()
                    else -> timelineResource.setGenericFailure()
                }
            }
        }
    }

}