package com.example.mynaoseioqueapp.data.repository

import com.example.mynaoseioqueapp.common.Resource
import com.example.mynaoseioqueapp.data.remote.FoodApi
import com.example.mynaoseioqueapp.domain.model.Food
import com.example.mynaoseioqueapp.domain.repository.FoodRepository
import java.lang.Exception
import javax.inject.Inject

class FoodRepoImpl @Inject constructor(
    private val api : FoodApi
) : FoodRepository{
    override suspend fun getFoods(authToken: String): Resource<List<Food>> {
        return try {
            val response = api.getAllFoods(authToken)
            val result = response.body()
            if(response.isSuccessful && result != null){
                Resource.Success(result)
            }else{
                Resource.Error(response.message())
            }
        } catch (e: Exception){
            Resource.Error(e.localizedMessage ?: "An unexpected error occured")
        }

    }
}