package com.rizky.journeyonsolo.ui.detail

import androidx.lifecycle.ViewModel
import com.rizky.journeyonsolo.data.DestinationRepository

class DetailViewModel(private val destinationRepository: DestinationRepository) : ViewModel() {

    fun getData(id: String) = destinationRepository.getDestinationId(id)

}