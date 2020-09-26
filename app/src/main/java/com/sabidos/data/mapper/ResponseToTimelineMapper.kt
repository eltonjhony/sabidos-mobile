package com.sabidos.data.mapper

import com.sabidos.data.remote.model.TimelineResponse
import com.sabidos.data.remote.model.WeeklyHitsResponse
import com.sabidos.domain.Timeline
import com.sabidos.domain.WeeklyHits

object ResponseToTimelineMapper : DataMapper<TimelineResponse, Timeline>() {

    override fun transform(entity: TimelineResponse): Timeline {
        return Timeline(
            entity.id,
            entity.description,
            ResponseToCategoryMapper.transform(entity.category),
            entity.date,
            entity.isCorrect
        )
    }

    override fun transform(entities: List<TimelineResponse>): List<Timeline> {
        return entities.map { transform(it) }
    }

}
