package com.sabidos.presentation.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabidos.domain.WeeklyHits
import com.sabidos.domain.interactor.GetWeeklyHitsUseCase
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResultWrapper
import com.sabidos.infrastructure.extensions.loading
import com.sabidos.infrastructure.extensions.setGenericFailure
import com.sabidos.infrastructure.extensions.setNetworkFailure
import com.sabidos.infrastructure.extensions.setSuccess
import kotlinx.coroutines.launch

class MyPerformanceViewModel(
    private val getWeeklyHitsUseCase: GetWeeklyHitsUseCase
) : ViewModel() {

    val weeklyHitsResource = MutableLiveData<Resource<List<WeeklyHits>>>()

    fun getWeeklyHitsFor(endDate: String) {
        weeklyHitsResource.loading()
        viewModelScope.launch {
            getWeeklyHitsUseCase(GetWeeklyHitsUseCase.Params(endDate)) {
                when (it) {
                    is ResultWrapper.Success -> weeklyHitsResource.setSuccess(it.data)
                    is ResultWrapper.NetworkError -> weeklyHitsResource.setNetworkFailure()
                    else -> weeklyHitsResource.setGenericFailure()
                }
            }
        }

    }

}