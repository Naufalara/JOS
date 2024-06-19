package com.rizky.journeyonsolo.data.di

import android.content.Context
import com.rizky.journeyonsolo.data.DestinationRepository
import com.rizky.journeyonsolo.data.local.room.DestinationRoomDatabase
import com.rizky.journeyonsolo.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): DestinationRepository {
        val apiService = ApiConfig.getApiService()
        val database = DestinationRoomDatabase.getDatabase(context)
        val dao = database.destinationDao()
        return DestinationRepository.getInstance(apiService, dao)
    }
}