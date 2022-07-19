package com.example.mynaoseioqueapp.data.remote

import retrofit2.Response
import retrofit2.http.*

interface TableApi {
    @Headers("Content-Type: application/json")
    @PUT("api/table/occupy/{id}")
    suspend fun occupyTable(@Header ("Authorization") authToken:String,
                            @Path("id") id: Long) : Response<Long>

    @PUT("api/table/leave/{id}")
    suspend fun leaveTable(@Header ("Authorization") authToken:String,
                           @Path("id") id:Long) : Response<Void>
}