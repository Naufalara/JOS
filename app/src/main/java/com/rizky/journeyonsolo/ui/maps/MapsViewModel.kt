package com.rizky.journeyonsolo.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizky.journeyonsolo.data.DestinationRepository
import com.rizky.journeyonsolo.data.Result
import com.rizky.journeyonsolo.data.pref.DestinationLocations
import kotlinx.coroutines.launch

class MapsViewModel(private val destinationRepository: DestinationRepository) : ViewModel() {
    fun getLocationDestination() = destinationRepository.getAllDestination()
}