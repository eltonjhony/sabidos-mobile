package com.sabidos.data.repository

import com.sabidos.data.local.cache.AvatarCache
import com.sabidos.data.repository.datasources.CloudAvatarDataSource
import com.sabidos.data.repository.datasources.LocalAvatarDataSource
import com.sabidos.domain.UserAvatar
import com.sabidos.domain.repository.AvatarRepository
import com.sabidos.infrastructure.ResultWrapper
import com.sabidos.infrastructure.ResultWrapper.DataNotFoundError

class AvatarDataRepository(
    private val localAvatarDataSource: LocalAvatarDataSource,
    private val cloudAvatarDataSource: CloudAvatarDataSource,
    private val avatarCache: AvatarCache
) : AvatarRepository {

    override suspend fun getAllAvatars(): ResultWrapper<List<UserAvatar>> = when {
        avatarCache.hasExpired() -> fetchFromRemoteSource()
        else -> {
            when (val localAvatars = fetchFromLocalSource()) {
                is ResultWrapper.Success -> localAvatars
                else -> fetchFromRemoteSource()
            }
        }
    }

    private suspend fun fetchFromRemoteSource(): ResultWrapper<List<UserAvatar>> =
        when (val remoteAvatars = cloudAvatarDataSource.getAvatars()) {
            is ResultWrapper.Success -> remoteAvatars
            else -> {
                when (val localAvatars = fetchFromLocalSource()) {
                    is ResultWrapper.Success -> localAvatars
                    else -> remoteAvatars
                }
            }
        }

    private fun fetchFromLocalSource(): ResultWrapper<List<UserAvatar>> =
        localAvatarDataSource.getAll()?.let {
            if (it.isEmpty()) {
                DataNotFoundError
            } else ResultWrapper.Success(it)
        } ?: DataNotFoundError

}