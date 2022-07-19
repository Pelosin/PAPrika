package com.example.mynaoseioqueapp.domain.repository

import com.example.mynaoseioqueapp.common.Resource
import com.example.mynaoseioqueapp.data.remote.dto.CreateUserRequest
import com.example.mynaoseioqueapp.data.remote.dto.AuthenticationResponse
import com.example.mynaoseioqueapp.data.remote.dto.UserLoginRequest
import com.example.mynaoseioqueapp.data.remote.dto.UserResponse

interface UserRepository {
    suspend fun loginUserPostRequest(userLoginRequest: UserLoginRequest) : Resource<AuthenticationResponse>

    suspend fun createUserPostRequest(createUserRequest: CreateUserRequest) : Resource<AuthenticationResponse>

    suspend fun getUserWithToken(authToken: String) : Resource<UserResponse>
}