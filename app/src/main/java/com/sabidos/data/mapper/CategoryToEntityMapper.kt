package com.sabidos.data.mapper

import com.sabidos.data.local.entities.CategoryEntity
import com.sabidos.domain.Category

object CategoryToEntityMapper : DataMapper<Category, CategoryEntity>() {

    override fun transform(entity: Category): CategoryEntity {
        return CategoryEntity(
            entity.id,
            entity.description,
            entity.imageUrl
        )
    }

    override fun transform(entities: List<Category>): List<CategoryEntity> {
        return entities.map { transform(it) }
    }

}