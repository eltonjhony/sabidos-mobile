package com.sabidos.domain.repository

import com.sabidos.domain.RankingWrapper
import com.sabidos.infrastructure.ResultWrapper

interface RankingRepository {
    suspend fun getWeeklyRanking(endDate: String): ResultWrapper<RankingWrapper>
}