package com.sabidos.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sabidos.domain.Category
import com.sabidos.domain.Timeline
import com.sabidos.domain.interactor.GetAllCategoriesUseCase
import com.sabidos.domain.interactor.GetCurrentAccountUseCase
import com.sabidos.domain.interactor.GetTimelineUseCase
import com.sabidos.domain.interactor.None
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
    private val getTimelineUseCase: GetTimelineUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase
) : AccountViewModel(getCurrentAccountUseCase) {

    val categoriesResource = MutableLiveData<Resource<List<Category>>>()
    val timelineResource = MutableLiveData<Resource<List<Timeline>>>()

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

    fun loadCategories() {
        categoriesResource.loading()
        viewModelScope.launch {
            getAllCategoriesUseCase(None()) {
                when (it) {
                    is Success -> categoriesResource.setSuccess(it.data)
                    is NetworkError -> categoriesResource.setNetworkFailure()
                    else -> categoriesResource.setGenericFailure()
                }
            }
        }
    }

}