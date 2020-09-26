package com.sabidos.data.mapper

import com.sabidos.data.remote.model.UserAvatarResponse
import com.sabidos.domain.UserAvatar

object ResponseToAccountAvatarMapper : DataMapper<UserAvatarResponse, UserAvatar>() {

    override fun transform(entity: UserAvatarResponse): UserAvatar {
        return UserAvatar(entity.id, entity.imageUrl)
    }

    override fun transform(entities: List<UserAvatarResponse>): List<UserAvatar> {
        return entities.map { transform(it) }
    }

}