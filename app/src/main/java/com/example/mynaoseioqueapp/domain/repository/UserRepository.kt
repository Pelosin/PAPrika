package com.example.mynaoseioqueapp.domain.repository

import com.example.mynaoseioqueapp.common.Resource
import com.example.mynaoseioqueapp.data.remote.dto.UserLoginResponse

interface UserRepository {
    suspend fun loginUserPostRequest(username:String, password:String) : Resource<UserLoginResponse>
}