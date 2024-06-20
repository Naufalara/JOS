package com.rizky.journeyonsolo.ui.register

import androidx.lifecycle.ViewModel
import com.rizky.journeyonsolo.data.DestinationRepository

class RegisterViewModel(private val destinationRepository: DestinationRepository) : ViewModel() {

    fun registerUser(username: String, email: String, password: String) = destinationRepository.registerUser(username, email, password)

}