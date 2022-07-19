package com.example.mynaoseioqueapp.domain.model

import java.io.Serializable
import java.math.BigDecimal

data class Food(
    val name: String,
    val description: String,
    val price: BigDecimal,
    val url: String,
) : Serializable
