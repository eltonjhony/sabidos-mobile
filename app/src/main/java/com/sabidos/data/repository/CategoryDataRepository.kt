package com.sabidos.data.repository

import com.sabidos.data.local.cache.CategoryCache
import com.sabidos.data.repository.datasources.CloudCategoryDataSource
import com.sabidos.data.repository.datasources.LocalCategoryDataSource
import com.sabidos.domain.Category
import com.sabidos.domain.repository.CategoryRepository
import com.sabidos.infrastructure.ResultWrapper
import com.sabidos.infrastructure.ResultWrapper.DataNotFoundError

class CategoryDataRepository(
    private val localCategoryDataSource: LocalCategoryDataSource,
    private val cloudCategoryDataSource: CloudCategoryDataSource,
    private val categoryCache: CategoryCache
) : CategoryRepository {

    override suspend fun getAllCategories(): ResultWrapper<List<Category>> = when {
        categoryCache.hasExpired() -> fetchFromRemoteSource()
        else -> {
            when (val localCategory = fetchFromLocalSource()) {
                is ResultWrapper.Success -> localCategory
                else -> fetchFromRemoteSource()
            }
        }
    }

    private suspend fun fetchFromRemoteSource(): ResultWrapper<List<Category>> =
        when (val remoteCategory = cloudCategoryDataSource.getCategories()) {
            is ResultWrapper.Success -> remoteCategory
            else -> {
                when (val localCategory = fetchFromLocalSource()) {
                    is ResultWrapper.Success -> localCategory
                    else -> remoteCategory
                }
            }
        }

    private fun fetchFromLocalSource(): ResultWrapper<List<Category>> =
        localCategoryDataSource.getAll()?.let {
            if (it.isEmpty()) {
                DataNotFoundError
            } else ResultWrapper.Success(it)
        } ?: DataNotFoundError

}