package com.jos.journeyonsolo.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jos.journeyonsolo.data.local.entity.FavoriteDestination

@Database(entities = [FavoriteDestination::class], version = 1, exportSchema = false)
abstract class DestinationRoomDatabase : RoomDatabase() {

    abstract fun destinationDao() : DestinationDao

    companion object {
        @Volatile
        private var INSTANCE: DestinationRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): DestinationRoomDatabase {
            if (INSTANCE == null) {
                synchronized(DestinationRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        DestinationRoomDatabase::class.java, "destination_database")
                        .build()
                }
            }
            return INSTANCE as DestinationRoomDatabase
        }
    }

}