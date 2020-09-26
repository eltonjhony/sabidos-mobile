package com.sabidos.data.mapper

import com.sabidos.data.remote.model.WeeklyHitsResponse
import com.sabidos.domain.WeeklyHits

object ResponseToWeeklyHitsMapper : DataMapper<WeeklyHitsResponse, WeeklyHits>() {

    override fun transform(entity: WeeklyHitsResponse): WeeklyHits {
        return WeeklyHits(entity.date, entity.value)
    }

    override fun transform(entities: List<WeeklyHitsResponse>): List<WeeklyHits> {
        return entities.map { transform(it) }
    }

}
