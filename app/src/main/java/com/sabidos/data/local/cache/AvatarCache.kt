package com.sabidos.data.local.cache

import android.content.SharedPreferences
import com.sabidos.data.local.AvatarDao
import com.sabidos.data.local.entities.AvatarEntity
import com.sabidos.infrastructure.logging.Logger

class AvatarCache(
    private val avatarDao: AvatarDao,
    apiPrefsManager: SharedPreferences
) : Cache<AvatarEntity>(apiPrefsManager) {

    override fun createCachingTag(): String = "com.sabidos.data.local.cache.avatar"

    override fun getAll(): List<AvatarEntity> =
        avatarDao.getAll()

    override fun putAll(entities: List<AvatarEntity>) =
        entities.forEach { put(it) }

    override fun put(entity: AvatarEntity) {
        runCatching {
            avatarDao.insert(entity)
            cache()
        }.onFailure {
            Logger.withTag(AvatarCache::class.java.simpleName).withCause(it)
        }
    }

}