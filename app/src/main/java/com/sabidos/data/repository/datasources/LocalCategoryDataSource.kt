package com.sabidos.data.repository.datasources

import com.sabidos.data.local.cache.CategoryCache
import com.sabidos.data.mapper.EntityToCategoryMapper
import com.sabidos.domain.Category

class LocalCategoryDataSource(private val categoryCache: CategoryCache) {

    fun getAll(): List<Category>? = runCatching {
        EntityToCategoryMapper.transform(categoryCache.getAll())
    }.getOrNull()

}