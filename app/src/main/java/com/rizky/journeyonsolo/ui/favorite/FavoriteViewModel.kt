package com.rizky.journeyonsolo.ui.favorite

import androidx.lifecycle.ViewModel
import com.rizky.journeyonsolo.data.DestinationRepository

class FavoriteViewModel(private val destinationRepository: DestinationRepository) : ViewModel() {

    fun getData() = destinationRepository.getAllDestination()

}