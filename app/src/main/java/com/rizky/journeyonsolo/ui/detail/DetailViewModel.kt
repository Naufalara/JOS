package com.rizky.journeyonsolo.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizky.journeyonsolo.data.DestinationRepository
import com.rizky.journeyonsolo.data.Result
import com.rizky.journeyonsolo.data.remote.response.DetailDestinationResponse
import kotlinx.coroutines.launch

class DetailViewModel(private val destinationRepository: DestinationRepository) : ViewModel() {

    fun getData(id: String) = destinationRepository.getDestinationId(id)

}