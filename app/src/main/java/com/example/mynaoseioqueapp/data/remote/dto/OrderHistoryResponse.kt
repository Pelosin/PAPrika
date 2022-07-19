package com.example.mynaoseioqueapp.data.remote.dto

import com.example.mynaoseioqueapp.domain.model.LineOrder
import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderHistoryResponse(
    val id: Long,
    val price: BigDecimal,
    val tableServed: String,
    val createdDate: String
)
