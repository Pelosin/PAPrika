package com.example.mynaoseioqueapp.data.repository

import com.example.mynaoseioqueapp.common.Resource
import com.example.mynaoseioqueapp.data.remote.TableApi
import com.example.mynaoseioqueapp.domain.repository.TableRepository
import java.lang.Exception
import javax.inject.Inject

class TableRepoImpl @Inject constructor(
    private val api: TableApi
) : TableRepository{
    override suspend fun occupyTable(
        authToken: String,
        url: String
    ): Resource<Long> {
        return try {
            val response = api.occupyTable(authToken, url)
            val result = response.body()
            if(response.isSuccessful && result != null){
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        }catch (e:Exception){
            Resource.Error(e.localizedMessage ?: "An unexpected error occured")
        }
    }
}