package com.example.mynaoseioqueapp.data.remote

import com.example.mynaoseioqueapp.data.remote.dto.CreateUserRequest
import com.example.mynaoseioqueapp.data.remote.dto.AuthenticationResponse
import com.example.mynaoseioqueapp.data.remote.dto.UserLoginRequest
import com.example.mynaoseioqueapp.data.remote.dto.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApi {
    @POST("api/login")
    suspend fun loginUserPostRequest(
        @Body userLoginRequest: UserLoginRequest
    ) : Response<AuthenticationResponse>

    @POST("api/user/save")
    suspend fun createUserPostRequest(
        @Body createUser: CreateUserRequest
    ) : Response<AuthenticationResponse>

    @GET("api/token/user")
    suspend fun getUserWithToken(
        @Header ("Authorization") authToken: String
    ) : Response<UserResponse>

}