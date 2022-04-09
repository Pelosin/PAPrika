package com.example.mynaoseioqueapp.domain.repository

import com.example.mynaoseioqueapp.common.Resource
import com.example.mynaoseioqueapp.domain.model.Order

interface OrderRepository {
    suspend fun saveOrder(authToken: String, order: Order): Resource<Order>
}