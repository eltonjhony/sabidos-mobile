package com.sabidos.data.repository.datasources

import com.sabidos.data.local.cache.CategoryCache
import com.sabidos.data.mapper.CategoryToEntityMapper
import com.sabidos.data.mapper.ResponseToCategoryMapper
import com.sabidos.data.remote.CategoryService
import com.sabidos.data.remote.CloudApiFactory
import com.sabidos.data.remote.ErrorHandling
import com.sabidos.data.remote.NetworkHandler
import com.sabidos.domain.Category
import com.sabidos.infrastructure.ResultWrapper

class CloudCategoryDataSource(
    private val cloudApiFactory: CloudApiFactory,
    private val networkHandler: NetworkHandler,
    private val categoryCache: CategoryCache
) {

    suspend fun getCategories(): ResultWrapper<List<Category>> = when {
        networkHandler.isConnected -> {
            runCatching {
                val service = cloudApiFactory.create(CategoryService::class.java)
                val response = service.getCategories()
                val categories = ResponseToCategoryMapper.transform(response.categories)
                categoryCache.putAll(CategoryToEntityMapper.transform(categories))
                ResultWrapper.Success(categories)
            }.getOrElse { ErrorHandling.parse(it) }
        }
        else -> ResultWrapper.NetworkError
    }

}