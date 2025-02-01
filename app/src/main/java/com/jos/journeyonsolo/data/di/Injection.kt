package com.jos.journeyonsolo.data.di

import android.content.Context
import com.jos.journeyonsolo.data.DestinationRepository
import com.jos.journeyonsolo.data.local.room.DestinationRoomDatabase
import com.jos.journeyonsolo.data.pref.Session
import com.jos.journeyonsolo.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): DestinationRepository {
        val database = DestinationRoomDatabase.getDatabase(context)
        val pref = Session.getInstance(context.dataStore)
        val dao = database.destinationDao()
        return DestinationRepository.getInstance(pref ,dao)
    }
}