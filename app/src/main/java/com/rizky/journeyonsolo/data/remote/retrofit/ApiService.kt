package com.rizky.journeyonsolo.data.remote.retrofit

import com.rizky.journeyonsolo.data.remote.response.DetailDestinationResponse
import com.rizky.journeyonsolo.data.remote.response.ListDestinationItem
import com.rizky.journeyonsolo.data.remote.response.LoginResponse
import com.rizky.journeyonsolo.data.remote.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("signup")
    suspend fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("data")
    suspend fun getDestinations(): List<ListDestinationItem>

    @GET("data/{id}")
    suspend fun getDestinationDetails(
        @Path("id") id: String
    ): DetailDestinationResponse

}