package com.sabidos.presentation.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

class CategoryViewModel(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase
) : ViewModel() {

    var selectedCategory: Category? = null

    val categoriesResource = MutableLiveData<Resource<List<Category>>>()

    fun loadCategories() {
        categoriesResource.loading()
        getAllCategoriesUseCase(None()) {
            when (it) {
                is Success -> categoriesResource.setSuccess(it.data)
                is NetworkError -> categoriesResource.setNetworkFailure()
                else -> categoriesResource.setGenericFailure()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        getAllCategoriesUseCase.clear()
    }

}