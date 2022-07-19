package com.example.mynaoseioqueapp.data.remote.dto

import com.example.mynaoseioqueapp.domain.model.LineOrder
import java.math.BigDecimal

data class OrderByIdResponse(
    val createdDateTime: String,
    val id: Int,
    val lineOrderList: List<LineOrderResponse>,
    val price: BigDecimal,
)