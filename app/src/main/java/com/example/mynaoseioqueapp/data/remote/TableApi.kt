package com.example.mynaoseioqueapp.data.remote

import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Url

interface TableApi {
    @PUT
    suspend fun occupyTable(@Header ("Authorization") authToken:String, @Url url: String) : Response<Long>
}