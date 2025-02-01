package com.jos.journeyonsolo.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.jos.journeyonsolo.data.DestinationRepository
import com.jos.journeyonsolo.data.pref.UserModel

class HomeViewModel(private val destinationRepository: DestinationRepository) : ViewModel() {

    fun getData(context: Context) = destinationRepository.getAllDestination(context)

}