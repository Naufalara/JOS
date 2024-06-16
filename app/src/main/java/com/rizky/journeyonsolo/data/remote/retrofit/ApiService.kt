package com.rizky.journeyonsolo.data.remote.retrofit

import com.rizky.journeyonsolo.data.remote.response.DestinationResponse
import com.rizky.journeyonsolo.data.remote.response.DetailDestinationResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("data")
    suspend fun getDestinations(): DestinationResponse

    @GET("data/{id}")
    suspend fun getDestinationDetails(
        @Path("id") id: String
    ): DetailDestinationResponse

}