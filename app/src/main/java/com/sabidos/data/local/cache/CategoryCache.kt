package com.sabidos.data.local.cache

import android.content.SharedPreferences
import com.sabidos.data.local.CategoryDao
import com.sabidos.data.local.entities.CategoryEntity
import com.sabidos.infrastructure.logging.Logger

class CategoryCache(
    private val categoryDao: CategoryDao,
    apiPrefsManager: SharedPreferences
) : Cache<CategoryEntity>(apiPrefsManager) {

    override fun createCachingTag(): String = "com.sabidos.data.local.cache.category"

    override fun getAll(): List<CategoryEntity> {
        return categoryDao.getAll()
    }

    override fun putAll(entities: List<CategoryEntity>) {
        entities.forEach { put(it) }
    }

    override fun put(entity: CategoryEntity) {
        runCatching {
            categoryDao.insert(entity)
            cache()
        }.onFailure {
            Logger.withTag(CategoryCache::class.java.simpleName).withCause(it)
        }
    }

}