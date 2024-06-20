package com.rizky.journeyonsolo.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rizky.journeyonsolo.data.DestinationRepository
import com.rizky.journeyonsolo.data.pref.UserModel

class HomeViewModel(private val destinationRepository: DestinationRepository) : ViewModel() {

    fun getData() = destinationRepository.getAllDestination()

    fun getSession(): LiveData<UserModel> {
        return destinationRepository.getSession().asLiveData()
    }

}