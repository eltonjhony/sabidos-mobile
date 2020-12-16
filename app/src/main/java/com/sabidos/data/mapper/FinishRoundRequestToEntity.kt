package com.sabidos.data.mapper

import com.sabidos.data.local.entities.PostRoundEntity
import com.sabidos.data.remote.model.FinishRoundRequest

object FinishRoundRequestToEntity : DataMapper<FinishRoundRequest, PostRoundEntity>() {

    override fun transform(entity: FinishRoundRequest): PostRoundEntity {
        return PostRoundEntity(
            accumulateXps = entity.accumulateXp
        )
    }

    override fun transform(entities: List<FinishRoundRequest>): List<PostRoundEntity> {
        return entities.map { transform(it) }
    }

}
