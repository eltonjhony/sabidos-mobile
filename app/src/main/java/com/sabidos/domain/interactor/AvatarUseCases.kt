package com.sabidos.domain.interactor

import com.sabidos.domain.UserAvatar
import com.sabidos.domain.repository.AvatarRepository
import com.sabidos.infrastructure.ResultWrapper

class GetAllAvatarsUseCase(private val avatarRepository: AvatarRepository) :
    UseCase<List<UserAvatar>, None>() {

    override suspend fun run(params: None): ResultWrapper<List<UserAvatar>> =
        avatarRepository.getAllAvatars()
}