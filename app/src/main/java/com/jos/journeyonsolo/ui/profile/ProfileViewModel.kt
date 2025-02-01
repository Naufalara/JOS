package com.jos.journeyonsolo.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jos.journeyonsolo.data.DestinationRepository
import com.jos.journeyonsolo.data.pref.UserModel
import kotlinx.coroutines.launch

class ProfileViewModel (private val destinationRepository: DestinationRepository) : ViewModel() {

    fun getThemeSetting(): LiveData<Boolean>{
        return destinationRepository.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean){
        viewModelScope.launch {
            destinationRepository.saveThemeSetting(isDarkModeActive)
        }
    }

    fun logout(){
        viewModelScope.launch {
            destinationRepository.logout()
        }
    }
}