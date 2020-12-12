package com.sabidos.data.mapper

import com.sabidos.data.local.entities.AccountEntity
import com.sabidos.domain.Account

object AccountToEntityMapper : DataMapper<Account, AccountEntity>() {

    override fun transform(entity: Account): AccountEntity {
        return AccountEntity(
            entity.name,
            entity.nickname,
            entity.avatar?.let { AvatarToEntityMapper.transform(it) },
            ReputationToEntityMapper.transform(entity.reputation),
            entity.xpFactor,
            entity.totalAnswered,
            entity.totalHits
        )
    }

    override fun transform(entities: List<Account>): List<AccountEntity> {
        return entities.map { transform(it) }
    }

}