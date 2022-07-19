package com.example.mynaoseioqueapp.data.remote.dto

import com.example.mynaoseioqueapp.domain.model.User

data class CreateUserResponse(
    val access_token: String,
    val accountNonExpired: Boolean,
    val accountNonLocked: Boolean,
    val authorities: List<Any>,
    val credentialsNonExpired: Boolean,
    val enabled: Boolean,
    val id: Int,
    val name: String,
    val orderList: Any,
    val password: String,
    val refresh_token: String,
    val roles: List<Any>,
    val username: String
)

fun CreateUserResponse.toUser() : User {
    return User(
        username = username,
        password = password,
        accessToken = access_token,
        refreshToken = refresh_token
    )
}