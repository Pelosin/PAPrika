package com.example.mynaoseioqueapp.domain.repository

import com.example.mynaoseioqueapp.common.Resource
import com.example.mynaoseioqueapp.domain.model.Category

interface CategoryRepository {
    suspend fun getCategories(jwtToken: String) : Resource<List<Category>>
}