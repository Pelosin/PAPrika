package com.example.mynaoseioqueapp.domain.repository

import com.example.mynaoseioqueapp.common.Resource
import com.example.mynaoseioqueapp.data.remote.dto.OrderByIdResponse
import com.example.mynaoseioqueapp.data.remote.dto.OrderHistoryResponse
import com.example.mynaoseioqueapp.domain.model.Order

interface OrderRepository {
    suspend fun saveOrder(authToken: String, order: Order): Resource<Order>

    suspend fun getOrderFromUser(authToken: String) : Resource<List<OrderHistoryResponse>>

    suspend fun getOrderById(authToken: String, id: Long) : Resource<OrderByIdResponse>
}