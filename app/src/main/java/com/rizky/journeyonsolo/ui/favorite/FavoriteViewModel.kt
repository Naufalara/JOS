package com.rizky.journeyonsolo.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rizky.journeyonsolo.data.DestinationRepository
import com.rizky.journeyonsolo.data.local.entity.FavoriteDestination

class FavoriteViewModel(private val destinationRepository: DestinationRepository) : ViewModel() {

    fun getFavDestination(): LiveData<List<FavoriteDestination>> = destinationRepository.getFavoriteDestination()

}