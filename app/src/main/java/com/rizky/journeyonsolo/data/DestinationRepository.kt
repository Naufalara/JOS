package com.rizky.journeyonsolo.data

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.rizky.journeyonsolo.R
import com.rizky.journeyonsolo.data.pref.Destination
import com.rizky.journeyonsolo.data.pref.DestinationLocations
import com.rizky.journeyonsolo.data.remote.response.ListDestinationItem
import com.rizky.journeyonsolo.data.remote.retrofit.ApiService
import retrofit2.HttpException
import com.rizky.journeyonsolo.data.Result
import com.rizky.journeyonsolo.data.remote.response.DestinationResponse
import com.rizky.journeyonsolo.data.remote.response.DetailErrorResponse

class DestinationRepository(private val apiService: ApiService) {

    fun getAllDestination() = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getDestinations()
            emit(Result.Success(response.listDestination))
        } catch (e: HttpException) {
            val jsonString = e.response()?.errorBody()?.string()
            Log.d(TAG, "DestinationRepository: $jsonString")
            emit(Result.Error("Respon error ga ada"))
        } catch (e: Exception) {
            emit(Result.Error("Lost Connection"))
        }
    }

    fun getDestinationId(id: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getDestinationDetails(id)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonString = e.response()?.errorBody()?.string()
            Log.d(TAG, "DestinationRepository: $jsonString")
            val errorBody = Gson().fromJson(jsonString, DetailErrorResponse::class.java)
            emit(Result.Error(errorBody.detail))
        } catch (e: Exception) {
            emit(Result.Error("Lost Connection"))
        }
    }

    suspend fun searchDestination(keyword: String): List<ListDestinationItem> {
        // Simulate search process, you can replace this with actual implementation
//        return getAllDestination().filter { it.name.contains(keyword, ignoreCase = true)
        return emptyList()
    }

    companion object {

        private const val TAG = "DestinationRepository"

        @SuppressLint("StaticFieldLeak")
        private var instance: DestinationRepository? = null
        fun getInstance(
            apiService: ApiService
        ): DestinationRepository? {
            synchronized(this) {
                instance = DestinationRepository(apiService)
            }
            return instance
        }
    }

}