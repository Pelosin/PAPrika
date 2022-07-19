package com.example.mynaoseioqueapp.data.repository

import com.example.mynaoseioqueapp.common.Resource
import com.example.mynaoseioqueapp.data.remote.UserApi
import com.example.mynaoseioqueapp.data.remote.dto.CreateUserRequest
import com.example.mynaoseioqueapp.data.remote.dto.AuthenticationResponse
import com.example.mynaoseioqueapp.data.remote.dto.UserLoginRequest
import com.example.mynaoseioqueapp.data.remote.dto.UserResponse
import com.example.mynaoseioqueapp.domain.repository.UserRepository
import javax.inject.Inject
import kotlin.Exception

class UserRepoImpl @Inject constructor(
    private val api: UserApi
) : UserRepository {
    override suspend fun loginUserPostRequest(
        userLoginRequest: UserLoginRequest
    ): Resource<AuthenticationResponse> {
        return try {
            val response = api.loginUserPostRequest(userLoginRequest)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An unexpected error occured")
        }
    }

    override suspend fun createUserPostRequest(createUser: CreateUserRequest): Resource<AuthenticationResponse> {
        return try {
            val response = api.createUserPostRequest(createUser)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }

    override suspend fun getUserWithToken(authToken: String): Resource<UserResponse> {
        return try {
            val response = api.getUserWithToken(authToken)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }
}