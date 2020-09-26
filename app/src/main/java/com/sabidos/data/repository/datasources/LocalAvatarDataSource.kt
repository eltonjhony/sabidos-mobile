package com.sabidos.data.repository.datasources

import com.sabidos.data.local.cache.AvatarCache
import com.sabidos.data.mapper.EntityToAvatarMapper
import com.sabidos.domain.UserAvatar

class LocalAvatarDataSource(private val avatarCache: AvatarCache) {

    fun getAll(): List<UserAvatar>? = runCatching {
        EntityToAvatarMapper.transform(avatarCache.getAll())
    }.getOrNull()

}