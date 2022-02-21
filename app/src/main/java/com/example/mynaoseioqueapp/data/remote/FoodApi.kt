package com.example.mynaoseioqueapp.data.remote

import com.example.mynaoseioqueapp.domain.model.Food
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface FoodApi {
    @GET("/api/food")
    suspend fun getAllFoods(@Header("Authorization") authToken: String): Response<List<Food>>
}