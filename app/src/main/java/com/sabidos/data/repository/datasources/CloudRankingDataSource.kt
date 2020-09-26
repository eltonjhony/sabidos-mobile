package com.sabidos.data.repository.datasources

import com.sabidos.data.mapper.ResponseToRankingMapper
import com.sabidos.data.remote.*
import com.sabidos.domain.RankingWrapper
import com.sabidos.infrastructure.ResultWrapper

class CloudRankingDataSource(
    private val cloudApiFactory: CloudApiFactory,
    private val networkHandler: NetworkHandler
) {

    suspend fun getWeeklyRanking(
        nickname: String,
        endDate: String
    ): ResultWrapper<RankingWrapper> {
        return when {
            networkHandler.isConnected -> {
                runCatching {
                    val service = cloudApiFactory.create(RankingService::class.java)
                    val response = service.getWeeklyRanking(nickname, endDate)
                    val podium = ResponseToRankingMapper.transform(response.podium)
                    val regularPositions =
                        ResponseToRankingMapper.transform(response.regularPositions)
                    ResultWrapper.Success(RankingWrapper(podium, regularPositions))
                }.getOrElse { ErrorHandling.parse(it) }
            }
            else -> ResultWrapper.NetworkError
        }
    }

}