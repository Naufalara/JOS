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
import com.rizky.journeyonsolo.data.pref.UserModel
import com.rizky.journeyonsolo.data.pref.UserPreference
import retrofit2.HttpException
import com.rizky.journeyonsolo.data.remote.response.DetailErrorResponse
import com.rizky.journeyonsolo.data.remote.response.LoginErrorResponse
import com.rizky.journeyonsolo.data.remote.response.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DestinationRepository(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    private val destinationDao: DestinationDao
) {

    fun registerUser(username: String, email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(username, email, password)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonString = e.response()?.errorBody()?.string()
            Log.d(TAG, "DestinationRepository: $jsonString")
            val errorBody = Gson().fromJson(jsonString, RegisterResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        } catch (e: Exception) {
            emit(Result.Error("Lost Connection"))
        }
    }

    fun loginUser(email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            Log.d("LoginUser", "Response: $response")
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonString = e.response()?.errorBody()?.string()
            Log.d("LoginUser", "HttpException: $jsonString")
            Log.d(TAG, "DestinationRepository: $jsonString")
            val errorBody = Gson().fromJson(jsonString, LoginErrorResponse::class.java)
            val errorMessage = errorBody.detail.message
            emit(Result.Error(errorMessage))
        } catch (e: Exception) {
            emit(Result.Error("Lost Connection"))
        }
    }

    suspend fun saveSession(userModel: UserModel) = userPreference.saveSession(userModel)

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() = userPreference.logout()

    fun getThemeSetting(): Flow<Boolean> {
        return userPreference.getThemeSetting()
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) =
        userPreference.saveThemeSetting(isDarkModeActive)

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
            userPreference: UserPreference,
            destinationDao: DestinationDao
        ): DestinationRepository =
            instance ?: synchronized(this){
                instance ?: DestinationRepository(apiService, userPreference, destinationDao)
            }.also { instance = it }
    }

}