package com.example.mynaoseioqueapp.domain.model

data class User(
    val username : String,
    val password : String,
    val accessToken : String,
    val refreshToken : String
)
