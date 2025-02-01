package com.rizky.journeyonsolo.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizky.journeyonsolo.data.DestinationRepository
import com.rizky.journeyonsolo.data.local.entity.FavoriteDestination
import kotlinx.coroutines.launch

class DetailViewModel(private val destinationRepository: DestinationRepository) : ViewModel() {

    fun getData(id: String) = destinationRepository.getDestinationId(id)
    fun getIsFavorite(id: String): LiveData<Boolean> = destinationRepository.getIsFavorite(id)

    fun insertFavoriteDestination(favoriteDestination: FavoriteDestination){
        viewModelScope.launch {
            destinationRepository.setInsertFavoriteDestination(favoriteDestination)
        }
    }

    fun deleteFavoriteDestination(favoriteDestination: FavoriteDestination){
        viewModelScope.launch {
            destinationRepository.delete(favoriteDestination)
        }
    }

}