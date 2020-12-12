package com.sabidos.data.mapper

import com.sabidos.data.remote.model.AccountResponse
import com.sabidos.domain.Account

object ResponseToAccountMapper : DataMapper<AccountResponse, Account>() {

    override fun transform(entity: AccountResponse): Account {
        return Account(
            entity.name,
            entity.nickname,
            entity.avatar?.let { ResponseToAccountAvatarMapper.transform(it) },
            ResponseToReputationMapper.transform(entity.reputation),
            entity.xpFactor,
            entity.totalAnswered,
            entity.totalHits
        )
    }

    override fun transform(entities: List<AccountResponse>): List<Account> {
        return entities.map { transform(it) }
    }

}