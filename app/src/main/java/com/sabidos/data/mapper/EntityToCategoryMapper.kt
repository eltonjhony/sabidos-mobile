package com.sabidos.data.mapper

import com.sabidos.data.local.entities.CategoryEntity
import com.sabidos.domain.Category

object EntityToCategoryMapper : DataMapper<CategoryEntity, Category>() {

    override fun transform(entity: CategoryEntity): Category {
        return Category(
            entity.id,
            entity.description,
            entity.imageUrl
        )
    }

    override fun transform(entities: List<CategoryEntity>): List<Category> {
        return entities.map { transform(it) }
    }

}