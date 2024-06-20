package com.rizky.journeyonsolo.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rizky.journeyonsolo.data.DestinationRepository
import com.rizky.journeyonsolo.data.pref.UserModel

class OnBoardingViewModel(private val destinationRepository: DestinationRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel> = destinationRepository.getSession().asLiveData()

}