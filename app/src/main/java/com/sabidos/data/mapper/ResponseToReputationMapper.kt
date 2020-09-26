package com.sabidos.data.mapper

import com.sabidos.data.remote.model.ReputationResponse
import com.sabidos.domain.Reputation

object ResponseToReputationMapper : DataMapper<ReputationResponse, Reputation>() {

    override fun transform(entity: ReputationResponse): Reputation {
        return Reputation(entity.level, entity.stars)
    }

    override fun transform(entities: List<ReputationResponse>): List<Reputation> {
        return entities.map { transform(it) }
    }

}