package com.example.mynaoseioqueapp.data.repository

import com.example.mynaoseioqueapp.common.Resource
import com.example.mynaoseioqueapp.data.remote.OrderApi
import com.example.mynaoseioqueapp.data.remote.dto.OrderByIdResponse
import com.example.mynaoseioqueapp.data.remote.dto.OrderHistoryResponse
import com.example.mynaoseioqueapp.domain.model.Order
import com.example.mynaoseioqueapp.domain.repository.OrderRepository
import javax.inject.Inject
import kotlin.Exception

class OrderRepoImpl @Inject constructor(
    private val api: OrderApi
) : OrderRepository{

    override suspend fun saveOrder(authToken: String, order: Order): Resource<Order> {
        return try {

            val response = api.saveOrder(authToken, order)
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

    override suspend fun getOrderFromUser(authToken: String): Resource<List<OrderHistoryResponse>> {
        return try {
            val response = api.getOrderFromUser(authToken)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage)
        }
    }

    override suspend fun getOrderById(authToken: String, id: Long): Resource<OrderByIdResponse> {
        return try {
            val response = api.getOrderById(authToken, id)
            val result = response.body()
            if(response.isSuccessful && result != null){
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e : Exception) {
            Resource.Error(e.localizedMessage)
        }
    }
}