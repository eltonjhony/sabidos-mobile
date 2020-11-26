package com.sabidos.data.mapper

import com.sabidos.data.remote.model.CategoryResponse
import com.sabidos.domain.Category

object ResponseToCategoryMapper : DataMapper<CategoryResponse, Category>() {

    override fun transform(entity: CategoryResponse): Category {
        return Category(entity.id, entity.description, entity.imageUrl, entity.iconUrl)
    }

    override fun transform(entities: List<CategoryResponse>): List<Category> {
        return entities.map { transform(it) }
    }

}
