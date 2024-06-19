package com.rizky.journeyonsolo.data

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.rizky.journeyonsolo.data.local.entity.FavoriteDestination
import com.rizky.journeyonsolo.data.local.room.DestinationDao
import com.rizky.journeyonsolo.data.remote.response.ListDestinationItem
import com.rizky.journeyonsolo.data.remote.retrofit.ApiService
import retrofit2.HttpException
import com.rizky.journeyonsolo.data.remote.response.DetailErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class DestinationRepository(
    private val apiService: ApiService,
    private val destinationDao: DestinationDao
) {
    fun getAllDestination() = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getDestinations()
            emit(Result.Success(response))
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

    fun getFavoriteDestination(): LiveData<List<FavoriteDestination>> {
        return destinationDao.getFavoriteDestination()
    }

    suspend fun setInsertFavoriteDestination(destination: FavoriteDestination){
        coroutineScope {
            launch(Dispatchers.IO){
                destinationDao.insert(destination)
            }
        }
    }

    suspend fun delete(destination: FavoriteDestination){
        coroutineScope {
            launch(Dispatchers.IO){
                destinationDao.delete(destination)
            }
        }
    }

    fun getIsFavorite(id: String): LiveData<Boolean> = destinationDao.isFavoriteDestination(id)

    companion object {

        private const val TAG = "DestinationRepository"

        @SuppressLint("StaticFieldLeak")
        private var instance: DestinationRepository? = null
        fun getInstance(
            apiService: ApiService,
            destinationDao: DestinationDao
        ): DestinationRepository =
            instance ?: synchronized(this){
                instance ?: DestinationRepository(apiService, destinationDao)
            }.also { instance = it }
    }

}