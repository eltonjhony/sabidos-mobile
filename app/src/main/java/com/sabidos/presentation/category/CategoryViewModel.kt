package com.sabidos.presentation.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabidos.domain.Category
import com.sabidos.domain.interactor.GetAllCategoriesUseCase
import com.sabidos.domain.interactor.None
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResultWrapper.NetworkError
import com.sabidos.infrastructure.ResultWrapper.Success
import com.sabidos.infrastructure.extensions.loading
import com.sabidos.infrastructure.extensions.setGenericFailure
import com.sabidos.infrastructure.extensions.setNetworkFailure
import com.sabidos.infrastructure.extensions.setSuccess
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase
) : ViewModel() {

    val categoryWrapperResource = MutableLiveData<Resource<CategoryWrapper>>()

    fun loadCategories() {
        categoryWrapperResource.loading()
        viewModelScope.launch {
            getAllCategoriesUseCase(None()) {
                when (it) {
                    is Success -> {
                        handleSuccess(it.data)
                    }
                    is NetworkError -> categoryWrapperResource.setNetworkFailure()
                    else -> categoryWrapperResource.setGenericFailure()
                }
            }
        }
    }

    private fun handleSuccess(data: List<Category>) {
        val newest = data.takeLast(3).reversed()
        val tops = data.takeLast(3)
        categoryWrapperResource.setSuccess(CategoryWrapper(tops, newest, data))
    }

}

data class CategoryWrapper(
    val tops: List<Category>,
    val newest: List<Category>,
    val all: List<Category>
)