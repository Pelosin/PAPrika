package com.example.mynaoseioqueapp.domain.model

import java.math.BigDecimal

data class LineOrder(
    val name: String,
    var quantity: Int,
    var price: BigDecimal,
    var partialPrice: BigDecimal,
    var url: String
)
