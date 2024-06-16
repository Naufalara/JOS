package com.rizky.journeyonsolo.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizky.journeyonsolo.data.DestinationRepository
import com.rizky.journeyonsolo.data.Result
import com.rizky.journeyonsolo.data.pref.Destination
import com.rizky.journeyonsolo.data.remote.response.ListDestinationItem
import kotlinx.coroutines.launch

class SearchResultViewModel(private val destinationRepository: DestinationRepository) : ViewModel() {

    private val _searchResult = MutableLiveData<Result<List<ListDestinationItem>>>()
    val searchResult: LiveData<Result<List<ListDestinationItem>>> = _searchResult

    fun searchDestination(keyword: String) {
        viewModelScope.launch {
            _searchResult.value = Result.Loading
            try {
                val result = destinationRepository.searchDestination(keyword)
                _searchResult.value = Result.Success(result)
            } catch (e: Exception) {
                _searchResult.value = Result.Error(e.message.toString())
            }
        }
    }

}