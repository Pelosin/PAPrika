package com.example.mynaoseioqueapp.data.remote

import com.example.mynaoseioqueapp.domain.model.Category
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface CategoryApi {
    @GET("/api/category/all")
    suspend fun getAllCategories(@Header("Authorization") jwtToken: String) : Response<List<Category>>
}