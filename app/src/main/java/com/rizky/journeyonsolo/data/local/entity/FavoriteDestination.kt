package com.rizky.journeyonsolo.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "FavoriteDestination")
@Parcelize
data class FavoriteDestination(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("placeId")
    var placeId: String = "",

    @ColumnInfo("name")
    var name: String? = null,

    @ColumnInfo("imageUrl")
    var imageUrl: String? = null,

    @ColumnInfo("address")
    var address: String? = null,

    @ColumnInfo("rating")
    var rating: String? = null,
) : Parcelable
