package com.sabidos.data.mapper

import com.sabidos.data.local.entities.AvatarEntity
import com.sabidos.domain.UserAvatar

object AvatarToEntityMapper : DataMapper<UserAvatar, AvatarEntity>() {

    override fun transform(entity: UserAvatar): AvatarEntity {
        return AvatarEntity(entity.id, entity.imageUrl)
    }

    override fun transform(entities: List<UserAvatar>): List<AvatarEntity> {
        return entities.map { transform(it) }
    }

}