package com.rizky.journeyonsolo.data.remote.retrofit

import com.rizky.journeyonsolo.data.pref.LoginBodyRequest
import com.rizky.journeyonsolo.data.pref.RegisterBodyRequest
import com.rizky.journeyonsolo.data.remote.response.DestinationDetailResponse
import com.rizky.journeyonsolo.data.remote.response.ListDestinationItem
import com.rizky.journeyonsolo.data.remote.response.LoginResponse
import com.rizky.journeyonsolo.data.remote.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("signup")
    suspend fun register(@Body request: RegisterBodyRequest): RegisterResponse

    @POST("login")
    suspend fun login(@Body request: LoginBodyRequest): LoginResponse

    @GET("data")
    suspend fun getDestinations(): List<ListDestinationItem>

    @GET("data/{id}")
    suspend fun getDestinationDetails(
        @Path("id") id: String
    ): DestinationDetailResponse

}