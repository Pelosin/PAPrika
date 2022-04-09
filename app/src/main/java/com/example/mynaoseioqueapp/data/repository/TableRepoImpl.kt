package com.example.mynaoseioqueapp.data.repository

import android.util.Log
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
        id: Long
    ): Resource<Long> {
        return try {
            val response = api.occupyTable("Bearer $authToken", id)
            val result = response.body()
            if(response.isSuccessful && result != null){
                Resource.Success(result)
            } else {
                Log.d("MyTableRepoError", response.message() + response.code())
                Resource.Error(response.message())
            }
        }catch (e:Exception){
            Resource.Error(e.localizedMessage ?: "An unexpected error occured")
        }
    }
}