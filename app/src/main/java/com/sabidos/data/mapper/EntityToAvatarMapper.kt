package com.sabidos.data.mapper

import com.sabidos.data.local.entities.AvatarEntity
import com.sabidos.domain.UserAvatar

object EntityToAvatarMapper : DataMapper<AvatarEntity, UserAvatar>() {

    override fun transform(entity: AvatarEntity): UserAvatar {
        return UserAvatar(entity.id, entity.imageUrl)
    }

    override fun transform(entities: List<AvatarEntity>): List<UserAvatar> {
        return entities.map { transform(it) }
    }

}