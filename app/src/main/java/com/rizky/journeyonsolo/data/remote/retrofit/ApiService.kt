package com.rizky.journeyonsolo.data.remote.retrofit

import com.rizky.journeyonsolo.data.remote.response.DetailDestinationResponse
import com.rizky.journeyonsolo.data.remote.response.ListDestinationItem
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("data")
    suspend fun getDestinations(): List<ListDestinationItem>

    @GET("data/{id}")
    suspend fun getDestinationDetails(
        @Path("id") id: String
    ): DetailDestinationResponse

}