package com.example.mynaoseioqueapp.domain.repository

import com.example.mynaoseioqueapp.common.Resource
import com.example.mynaoseioqueapp.domain.model.Food

interface FoodRepository {
    suspend fun getFoods(authToken: String) : Resource<List<Food>>
}