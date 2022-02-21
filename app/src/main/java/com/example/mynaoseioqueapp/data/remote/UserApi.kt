package com.example.mynaoseioqueapp.data.remote

import com.example.mynaoseioqueapp.data.remote.dto.UserLoginResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {
    @POST("api/login")
    suspend fun loginUserPostRequest(
        @Query("username") username: String,
        @Query("password") password: String
    ) : Response<UserLoginResponse>
}