package com.sabidos.data.mapper

import com.sabidos.data.remote.model.AccountRequest
import com.sabidos.domain.Account

object AccountToRequestMapper : DataMapper<Account, AccountRequest>() {

    override fun transform(entity: Account): AccountRequest {
        return AccountRequest(
            entity.name,
            entity.nickname,
            entity.avatar?.id
        )
    }

    override fun transform(entities: List<Account>): List<AccountRequest> {
        return entities.map { transform(it) }
    }

}