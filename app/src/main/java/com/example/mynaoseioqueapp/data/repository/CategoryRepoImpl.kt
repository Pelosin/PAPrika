package com.example.mynaoseioqueapp.data.repository

import com.example.mynaoseioqueapp.common.Resource
import com.example.mynaoseioqueapp.data.remote.CategoryApi
import com.example.mynaoseioqueapp.domain.model.Category
import com.example.mynaoseioqueapp.domain.repository.CategoryRepository
import javax.inject.Inject

class CategoryRepoImpl @Inject constructor(
    private val api: CategoryApi
) : CategoryRepository{

    override suspend fun getCategories(jwtToken: String): Resource<List<Category>> {
        return try {
            val response = api.getAllCategories(jwtToken)
            val result = response.body()
            if(response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error")
        }
    }
}