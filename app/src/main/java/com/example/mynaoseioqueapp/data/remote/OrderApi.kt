package com.example.mynaoseioqueapp.data.remote

import com.example.mynaoseioqueapp.data.remote.dto.OrderByIdResponse
import com.example.mynaoseioqueapp.data.remote.dto.OrderHistoryResponse
import com.example.mynaoseioqueapp.domain.model.Order
import retrofit2.Response
import retrofit2.http.*

interface OrderApi {
    @POST("api/order")
    suspend fun saveOrder(@Header ("Authorization") authToken: String,  @Body order: Order) : Response<Order>

    @GET("api/order/user")
    suspend fun getOrderFromUser(@Header ("Authorization") authToken: String) : Response<List<OrderHistoryResponse>>

    @GET("api/order/{id}")
    suspend fun getOrderById(@Header ("Authorization") authToken: String,  @Path("id") id: Long) : Response<OrderByIdResponse>
}