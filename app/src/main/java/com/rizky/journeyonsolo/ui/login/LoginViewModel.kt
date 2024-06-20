package com.rizky.journeyonsolo.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizky.journeyonsolo.data.DestinationRepository
import com.rizky.journeyonsolo.data.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val destinationRepository: DestinationRepository) : ViewModel() {

    fun loginUser(email: String, password: String) = destinationRepository.loginUser(email, password)

}