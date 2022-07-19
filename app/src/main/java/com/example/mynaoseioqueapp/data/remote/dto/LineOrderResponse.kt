package com.example.mynaoseioqueapp.data.remote.dto

import com.example.mynaoseioqueapp.domain.model.Food
import java.math.BigDecimal

data class LineOrderResponse(
    val food: Food,
    var quantity: Int,
    var price: BigDecimal,
    var partialPrice: BigDecimal,
    var url: String
)
