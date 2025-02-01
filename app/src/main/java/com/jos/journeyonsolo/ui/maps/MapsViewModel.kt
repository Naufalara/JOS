package com.jos.journeyonsolo.ui.maps

import android.content.Context
import androidx.lifecycle.ViewModel
import com.jos.journeyonsolo.data.DestinationRepository

class MapsViewModel(private val destinationRepository: DestinationRepository) : ViewModel() {
    fun getAllDestinationLocation(context: Context) = destinationRepository.getAllDestination(context)
}