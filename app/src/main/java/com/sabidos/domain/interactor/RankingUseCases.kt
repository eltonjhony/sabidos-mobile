package com.sabidos.domain.interactor

import com.sabidos.domain.RankingWrapper
import com.sabidos.domain.interactor.GetWeeklyRankingUseCase.Params
import com.sabidos.domain.repository.RankingRepository
import com.sabidos.infrastructure.ResultWrapper

class GetWeeklyRankingUseCase(private val rankingRepository: RankingRepository) :
    UseCase<RankingWrapper, Params>() {

    override suspend fun run(params: Params): ResultWrapper<RankingWrapper> =
        rankingRepository.getWeeklyRanking(params.endDate)

    data class Params(val endDate: String)
}