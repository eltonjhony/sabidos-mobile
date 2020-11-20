package com.sabidos.data.mapper

import com.sabidos.data.local.entities.PostRoundEntity
import com.sabidos.data.remote.model.FinishRoundRequest

object RoundEntityToRequest : DataMapper<PostRoundEntity, FinishRoundRequest>() {

    override fun transform(entity: PostRoundEntity): FinishRoundRequest {
        return FinishRoundRequest(
            roundId = entity.roundId,
            averageResponseTime = entity.averageResponseTime,
            accumulateXp = entity.accumulateXps
        )
    }

    override fun transform(entities: List<PostRoundEntity>): List<FinishRoundRequest> {
        return entities.map { transform(it) }
    }

}
