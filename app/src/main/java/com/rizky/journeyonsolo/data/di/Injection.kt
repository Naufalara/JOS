package com.rizky.journeyonsolo.data.di

import android.content.Context
import com.rizky.journeyonsolo.data.DestinationRepository
import com.rizky.journeyonsolo.data.local.room.DestinationRoomDatabase
import com.rizky.journeyonsolo.data.pref.Session
import com.rizky.journeyonsolo.data.pref.dataStore
import com.rizky.journeyonsolo.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): DestinationRepository {
        val database = DestinationRoomDatabase.getDatabase(context)
        val pref = Session.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val dao = database.destinationDao()
        val apiService = ApiConfig.getApiService(user.token)
        return DestinationRepository.getInstance(apiService, pref ,dao)
    }
}