package com.sabidos.domain.repository

import com.sabidos.domain.Timeline
import com.sabidos.domain.Account
import com.sabidos.domain.User
import com.sabidos.domain.WeeklyHits
import com.sabidos.infrastructure.ResultWrapper

interface AccountRepository {
    suspend fun createAccount(account: Account, user: User): ResultWrapper<Account>
    suspend fun createAnonymousAccount(): ResultWrapper<Account>
    suspend fun getCurrentAccount(): ResultWrapper<Account>

    suspend fun getWeeklyHits(endDate: String): ResultWrapper<List<WeeklyHits>>

    suspend fun getTimeline(page: Int): ResultWrapper<List<Timeline>>
}