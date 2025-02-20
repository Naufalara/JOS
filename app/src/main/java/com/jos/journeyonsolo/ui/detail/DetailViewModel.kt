package com.jos.journeyonsolo.ui.detail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jos.journeyonsolo.data.DestinationRepository
import com.jos.journeyonsolo.data.local.entity.FavoriteDestination
import kotlinx.coroutines.launch

class DetailViewModel(private val destinationRepository: DestinationRepository) : ViewModel() {

    fun getData(id: String,context: Context) = destinationRepository.getDestinationId(id,context)

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