package com.sabidos.data.repository.datasources

import com.sabidos.data.local.cache.AvatarCache
import com.sabidos.data.mapper.AvatarToEntityMapper
import com.sabidos.data.mapper.ResponseToAccountAvatarMapper
import com.sabidos.data.remote.AvatarService
import com.sabidos.data.remote.CloudApiFactory
import com.sabidos.data.remote.ErrorHandling
import com.sabidos.data.remote.NetworkHandler
import com.sabidos.domain.UserAvatar
import com.sabidos.infrastructure.ResultWrapper

class CloudAvatarDataSource(
    private val cloudApiFactory: CloudApiFactory,
    private val networkHandler: NetworkHandler,
    private val avatarCache: AvatarCache
) {

    suspend fun getAvatars(): ResultWrapper<List<UserAvatar>> = when {
        networkHandler.isConnected -> {
            runCatching {
                val service = cloudApiFactory.create(AvatarService::class.java)
                val response = service.getAvatars()
                val avatars = ResponseToAccountAvatarMapper.transform(response.avatars)
                avatarCache.putAll(AvatarToEntityMapper.transform(avatars))
                ResultWrapper.Success(avatars)
            }.getOrElse { ErrorHandling.parse(it) }
        }
        else -> ResultWrapper.NetworkError
    }

}