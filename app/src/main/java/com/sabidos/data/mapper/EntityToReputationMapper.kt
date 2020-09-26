package com.sabidos.data.mapper

import com.sabidos.data.local.entities.ReputationEntity
import com.sabidos.domain.Reputation

object EntityToReputationMapper : DataMapper<ReputationEntity, Reputation>() {

    override fun transform(entity: ReputationEntity): Reputation {
        return Reputation(entity.level, entity.stars)
    }

    override fun transform(entities: List<ReputationEntity>): List<Reputation> {
        return entities.map { transform(it) }
    }

}