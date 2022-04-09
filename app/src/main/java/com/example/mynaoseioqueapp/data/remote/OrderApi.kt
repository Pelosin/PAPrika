package com.example.mynaoseioqueapp.data.remote

import com.example.mynaoseioqueapp.domain.model.Order
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OrderApi {
    @POST("api/order")
    suspend fun saveOrder(@Header ("Authorization") authToken: String,  @Body order: Order) : Response<Order>
}