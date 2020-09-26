package com.sabidos.data.local.cache

import android.content.SharedPreferences
import com.sabidos.data.local.AccountDao
import com.sabidos.data.local.entities.AccountEntity
import com.sabidos.infrastructure.logging.Logger

class AccountCache(
    private val accountDao: AccountDao,
    apiPrefsManager: SharedPreferences
) : Cache<AccountEntity>(apiPrefsManager) {

    override fun createCachingTag(): String = "com.sabidos.data.local.cache.account"

    override fun getAll(): List<AccountEntity> {
        TODO("Not yet implemented")
    }

    override fun putAll(entities: List<AccountEntity>) {
        entities.forEach { put(it) }
    }

    override fun put(entity: AccountEntity) {
        runCatching {
            accountDao.insert(entity)
            cache()
        }.onFailure {
            Logger.withTag(AccountCache::class.java.simpleName).withCause(it)
        }
    }

    fun getAccount(): AccountEntity = accountDao.getCurrentAccount()
    fun getNickname(): String = accountDao.getNickName()

}