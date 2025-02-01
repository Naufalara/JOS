package com.jos.journeyonsolo.data.pref

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Destination(
    val name: String,
    val photo: Int,
    val location: String,
    val rating: String,
    val description: String,
    val longitude: Float,
    val latitude: Float
) : Parcelable

@Parcelize
data class DestinationLocations(
    val name: String,
    val longitude: Float,
    val latitude: Float
) : Parcelable
