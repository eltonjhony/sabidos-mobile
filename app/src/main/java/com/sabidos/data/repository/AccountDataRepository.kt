package com.sabidos.data.repository

import com.sabidos.data.local.cache.AccountCache
import com.sabidos.data.mapper.EntityToAccountMapper
import com.sabidos.data.repository.datasources.CloudAccountDataSource
import com.sabidos.data.repository.datasources.LocalAccountDataSource
import com.sabidos.domain.Timeline
import com.sabidos.domain.Account
import com.sabidos.domain.User
import com.sabidos.domain.WeeklyHits
import com.sabidos.domain.repository.AccountRepository
import com.sabidos.infrastructure.ResultWrapper
import com.sabidos.infrastructure.ResultWrapper.*

class AccountDataRepository(
    private val localAccountDataSource: LocalAccountDataSource,
    private val cloudAccountDataSource: CloudAccountDataSource,
    private val accountCache: AccountCache
) : AccountRepository {

    override suspend fun createAccount(account: Account, user: User): ResultWrapper<Account> =
        cloudAccountDataSource.createAccount(account, user)

    override suspend fun validateAccount(nickname: String): ResultWrapper<Boolean> =
        cloudAccountDataSource.validateAccount(nickname)

    override suspend fun createAnonymousAccount(): ResultWrapper<Account> =
        cloudAccountDataSource.createAnonymousAccount()

    override suspend fun getCurrentAccount(): ResultWrapper<Account> = when {
        accountCache.hasExpired() -> {
            localAccountDataSource.getCurrentNickname()?.let {

                when (val remoteAccount = cloudAccountDataSource.getAccountBy(it)) {
                    is Success -> remoteAccount
                    else -> {
                        when (val localAccount = fetchFromLocalSource()) {
                            is Success -> localAccount
                            else -> remoteAccount
                        }
                    }
                }

            } ?: cloudAccountDataSource.getAccountByUserUid()

        }
        else -> {
            fetchFromLocalSource()
        }
    }

    override suspend fun getWeeklyHits(endDate: String): ResultWrapper<List<WeeklyHits>> =
        localAccountDataSource.getCurrentNickname()?.let {
            cloudAccountDataSource.getWeeklyHitsBy(it, endDate)
        } ?: DataNotFoundError

    override suspend fun getTimeline(page: Int): ResultWrapper<List<Timeline>> =
        localAccountDataSource.getCurrentNickname()?.let {
            cloudAccountDataSource.getTimelineFor(it, page)
        } ?: DataNotFoundError

    private fun fetchFromLocalSource(): ResultWrapper<Account> =
        localAccountDataSource.getCurrentAccount()?.let {
            Success(EntityToAccountMapper.transform(it))
        } ?: DataNotFoundError

}