package com.sabidos.data.mapper

import com.sabidos.data.local.entities.AccountEntity
import com.sabidos.domain.Account

object EntityToAccountMapper : DataMapper<AccountEntity, Account>() {

    override fun transform(entity: AccountEntity): Account {
        return Account(
            entity.name,
            entity.nickname,
            entity.avatar?.let { EntityToAvatarMapper.transform(it) },
            EntityToReputationMapper.transform(entity.reputation),
            entity.totalAnswered,
            entity.totalHits
        )
    }

    override fun transform(entities: List<AccountEntity>): List<Account> {
        return entities.map { transform(it) }
    }

}