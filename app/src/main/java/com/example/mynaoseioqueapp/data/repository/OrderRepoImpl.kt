package com.example.mynaoseioqueapp.data.repository

import com.example.mynaoseioqueapp.common.Resource
import com.example.mynaoseioqueapp.data.remote.OrderApi
import com.example.mynaoseioqueapp.domain.model.Order
import com.example.mynaoseioqueapp.domain.repository.OrderRepository
import java.lang.Exception
import javax.inject.Inject

class OrderRepoImpl @Inject constructor(
    private val api: OrderApi
) : OrderRepository{

    override suspend fun saveOrder(authToken: String, order: Order): Resource<Order> {
        return try {
            val response = api.saveOrder("Bearer $authToken", order)
            val result = response.body()
            if(response.isSuccessful && result != null){
                Resource.Success(result)
            }else{
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage)
        }
    }
}