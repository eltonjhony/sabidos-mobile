package com.sabidos.domain.interactor

import com.sabidos.domain.Category
import com.sabidos.domain.repository.CategoryRepository
import com.sabidos.infrastructure.ResultWrapper

class GetAllCategoriesUseCase(private val categoryRepository: CategoryRepository) :
    UseCase<List<Category>, None>() {

    override suspend fun run(params: None): ResultWrapper<List<Category>> =
        categoryRepository.getAllCategories()
}