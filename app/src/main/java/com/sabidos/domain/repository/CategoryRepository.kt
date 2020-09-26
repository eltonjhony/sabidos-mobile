package com.sabidos.domain.repository

import com.sabidos.domain.Category
import com.sabidos.infrastructure.ResultWrapper

interface CategoryRepository {
    suspend fun getAllCategories(): ResultWrapper<List<Category>>
}