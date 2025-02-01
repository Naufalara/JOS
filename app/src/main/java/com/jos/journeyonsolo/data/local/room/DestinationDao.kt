package com.jos.journeyonsolo.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jos.journeyonsolo.data.local.entity.FavoriteDestination

@Dao
interface DestinationDao {

    @Query("SELECT * FROM favoritedestination ORDER BY placeId DESC ")
    fun getFavoriteDestination(): LiveData<List<FavoriteDestination>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteDestination: FavoriteDestination)

    @Delete
    suspend fun delete(favoriteDestination: FavoriteDestination)

    @Query("SELECT EXISTS (SELECT * FROM favoritedestination WHERE placeId = :placeId)")
    fun isFavoriteDestination(placeId: String): LiveData<Boolean>

}