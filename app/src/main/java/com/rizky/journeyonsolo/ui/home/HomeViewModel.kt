package com.rizky.journeyonsolo.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizky.journeyonsolo.data.pref.Destination
import com.rizky.journeyonsolo.data.DestinationRepository
import com.rizky.journeyonsolo.data.Result
import com.rizky.journeyonsolo.data.remote.response.ListDestinationItem
import kotlinx.coroutines.launch

class HomeViewModel(private val destinationRepository: DestinationRepository) : ViewModel() {

    fun getData() = destinationRepository.getAllDestination()

}