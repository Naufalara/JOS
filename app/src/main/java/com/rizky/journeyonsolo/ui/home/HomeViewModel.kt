package com.rizky.journeyonsolo.ui.home

import androidx.lifecycle.ViewModel
import com.rizky.journeyonsolo.data.DestinationRepository

class HomeViewModel(private val destinationRepository: DestinationRepository) : ViewModel() {

    fun getData() = destinationRepository.getAllDestination()

}