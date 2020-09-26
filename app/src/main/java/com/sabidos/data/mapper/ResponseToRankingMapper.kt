package com.sabidos.data.mapper

import com.sabidos.data.remote.model.RankingResponse
import com.sabidos.domain.Ranking

object ResponseToRankingMapper : DataMapper<RankingResponse, Ranking>() {

    override fun transform(entity: RankingResponse): Ranking {
        return Ranking(
            entity.position,
            entity.avatar?.let { ResponseToAccountAvatarMapper.transform(it) },
            entity.player.firstName,
            entity.player.nickname,
            ResponseToReputationMapper.transform(entity.player.reputation),
            entity.totalHits,
            entity.isMyPosition
        )
    }

    override fun transform(entities: List<RankingResponse>): List<Ranking> {
        return entities.map { transform(it) }
    }

}