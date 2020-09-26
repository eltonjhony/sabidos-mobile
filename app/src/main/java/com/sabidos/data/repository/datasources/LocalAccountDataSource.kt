package com.sabidos.data.repository.datasources

import com.sabidos.data.local.cache.AccountCache
import com.sabidos.data.local.entities.AccountEntity

class LocalAccountDataSource(private val accountCache: AccountCache) {

    fun getCurrentAccount(): AccountEntity? = runCatching {
        accountCache.getAccount()
    }.getOrNull()

    fun getCurrentNickname(): String? = runCatching {
        accountCache.getNickname()
    }.getOrNull()

}