package com.rizky.journeyonsolo.ui.maps

import androidx.lifecycle.ViewModel
import com.rizky.journeyonsolo.data.DestinationRepository

class MapsViewModel(private val destinationRepository: DestinationRepository) : ViewModel() {
    fun getLocationDestination() = destinationRepository.getAllDestination()
}