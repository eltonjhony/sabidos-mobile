package com.sabidos.data.repository

import com.sabidos.data.repository.datasources.CloudRankingDataSource
import com.sabidos.data.repository.datasources.LocalAccountDataSource
import com.sabidos.domain.RankingWrapper
import com.sabidos.domain.repository.RankingRepository
import com.sabidos.infrastructure.ResultWrapper
import com.sabidos.infrastructure.ResultWrapper.DataNotFoundError

class RankingDataRepository(
    private val localAccountDataSource: LocalAccountDataSource,
    private val cloudRankingDataSource: CloudRankingDataSource
) : RankingRepository {

    override suspend fun getWeeklyRanking(endDate: String): ResultWrapper<RankingWrapper> =
        localAccountDataSource.getCurrentNickname()?.let {
            cloudRankingDataSource.getWeeklyRanking(it, endDate)
        } ?: DataNotFoundError

}