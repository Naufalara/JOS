package com.rizky.journeyonsolo.data

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.rizky.journeyonsolo.data.local.entity.FavoriteDestination
import com.rizky.journeyonsolo.data.local.room.DestinationDao
import com.rizky.journeyonsolo.data.pref.LoginBodyRequest
import com.rizky.journeyonsolo.data.pref.RegisterBodyRequest
import com.rizky.journeyonsolo.data.remote.response.ListDestinationItem
import com.rizky.journeyonsolo.data.remote.retrofit.ApiService
import com.rizky.journeyonsolo.data.pref.UserModel
import com.rizky.journeyonsolo.data.pref.Session
import retrofit2.HttpException
import com.rizky.journeyonsolo.data.remote.response.DestinationDetailErrorResponse
import com.rizky.journeyonsolo.data.remote.response.ErrorLoginRegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DestinationRepository(
    private val apiService: ApiService,
    private val session: Session,
    private val destinationDao: DestinationDao
) {

    fun registerUser(username: String, email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val bodyRequest = RegisterBodyRequest(username, email, password)
            val response = apiService.register(bodyRequest)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonString = e.response()?.errorBody()?.string()
            Log.d(TAG, "DestinationRepository: $jsonString")
            val errorBody = Gson().fromJson(jsonString, ErrorLoginRegisterResponse::class.java)
            val errorMessage = errorBody.detail.message
            emit(Result.Error(errorMessage))
        } catch (e: Exception) {
            emit(Result.Error("Lost Connection"))
        }
    }

    fun loginUser(email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val bodyRequest = LoginBodyRequest(email, password)
            val response = apiService.login(bodyRequest)
            saveSession(UserModel(response.loginResult.username, response.loginResult.token,true))
            Log.d("LoginUser", "Response: $response")
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonString = e.response()?.errorBody()?.string()
            Log.d("LoginUser", "HttpException: $jsonString")
            Log.d(TAG, "DestinationRepository: $jsonString")
            val errorBody = Gson().fromJson(jsonString, ErrorLoginRegisterResponse::class.java)
            val errorMessage = errorBody.detail.message
            emit(Result.Error(errorMessage))
        } catch (e: Exception) {
            emit(Result.Error("Lost Connection"))
        }
    }

    private suspend fun saveSession(userModel: UserModel) = session.saveSession(userModel)

    fun getSession(): Flow<UserModel> {
        return session.getSession()
    }

    suspend fun logout() = session.logout()

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
            val errorBody = Gson().fromJson(jsonString, DestinationDetailErrorResponse::class.java)
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

    fun getThemeSetting(): Flow<Boolean> {
        return session.getThemeSetting()
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) =
        session.saveThemeSetting(isDarkModeActive)

    fun getIsFavorite(id: String): LiveData<Boolean> = destinationDao.isFavoriteDestination(id)

    companion object {

        private const val TAG = "DestinationRepository"

        @SuppressLint("StaticFieldLeak")
        private var instance: DestinationRepository? = null
        fun getInstance(
            apiService: ApiService,
            session: Session,
            destinationDao: DestinationDao
        ): DestinationRepository =
            instance ?: synchronized(this){
                instance ?: DestinationRepository(apiService, session, destinationDao)
            }.also { instance = it }
    }

}