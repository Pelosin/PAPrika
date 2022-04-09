package com.example.mynaoseioqueapp.domain.repository

import com.example.mynaoseioqueapp.common.Resource

interface TableRepository {
    suspend fun occupyTable(authToken: String, id:Long) : Resource<Long>
}