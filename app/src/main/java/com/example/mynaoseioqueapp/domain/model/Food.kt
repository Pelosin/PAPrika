package com.example.mynaoseioqueapp.domain.model

import java.io.Serializable
import java.math.BigDecimal

data class Food(
    val name: String,
    val desc: String,
    val price: BigDecimal
) : Serializable
