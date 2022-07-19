package com.example.mynaoseioqueapp.domain.model

import com.example.mynaoseioqueapp.data.remote.dto.LineOrderRequest
import java.math.BigDecimal

data class Order(
    val lineOrderRequestList: List<LineOrderRequest>,
    val price: BigDecimal,
    val tableId: Long,
    val token: String
)
