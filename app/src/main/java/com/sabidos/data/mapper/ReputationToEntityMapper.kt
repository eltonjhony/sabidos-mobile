package com.sabidos.data.mapper

import com.sabidos.data.local.entities.ReputationEntity
import com.sabidos.domain.Reputation

object ReputationToEntityMapper : DataMapper<Reputation?, ReputationEntity>() {

    override fun transform(entity: Reputation?): ReputationEntity {
        val reputation = entity ?: Reputation.defaultEmpty()
        return ReputationEntity(
            reputation.level, reputation.stars
        )
    }

    override fun transform(entities: List<Reputation?>): List<ReputationEntity> {
        return entities.map { transform(it) }
    }

}