package com.sabidos.domain.repository

import com.sabidos.domain.UserAvatar
import com.sabidos.infrastructure.ResultWrapper

interface AvatarRepository {
    suspend fun getAllAvatars(): ResultWrapper<List<UserAvatar>>
}