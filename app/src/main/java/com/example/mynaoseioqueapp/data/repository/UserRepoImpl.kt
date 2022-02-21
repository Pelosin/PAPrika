package com.example.mynaoseioqueapp.data.repository

import com.example.mynaoseioqueapp.common.Resource
import com.example.mynaoseioqueapp.data.remote.UserApi
import com.example.mynaoseioqueapp.data.remote.dto.UserLoginResponse
import com.example.mynaoseioqueapp.domain.repository.UserRepository
import java.lang.Exception
import javax.inject.Inject

class UserRepoImpl @Inject constructor(
    private val api : UserApi
) : UserRepository{
    override suspend fun loginUserPostRequest(
        username: String,
        password: String
    ): Resource<UserLoginResponse> {
        return try {
            val response = api.loginUserPostRequest(username, password)
            val result = response.body()
            if(response.isSuccessful && result != null){
                Resource.Success(result)
            }else{
                Resource.Error(response.message())
            }
        } catch (e:Exception){
            Resource.Error(e.localizedMessage ?: "An unexpected")
        }
    }
}