package com.rizky.journeyonsolo.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rizky.journeyonsolo.data.DestinationRepository
import com.rizky.journeyonsolo.data.di.Injection
import com.rizky.journeyonsolo.ui.detail.DetailViewModel
import com.rizky.journeyonsolo.ui.favorite.FavoriteViewModel
import com.rizky.journeyonsolo.ui.home.HomeViewModel
import com.rizky.journeyonsolo.ui.login.LoginViewModel
import com.rizky.journeyonsolo.ui.maps.MapsViewModel
import com.rizky.journeyonsolo.ui.onboarding.OnBoardingViewModel
import com.rizky.journeyonsolo.ui.profile.ProfileViewModel
import com.rizky.journeyonsolo.ui.register.RegisterViewModel
import com.rizky.journeyonsolo.ui.search.SearchResultViewModel

class ViewModelFactory(private val repository: DestinationRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SearchResultViewModel::class.java) -> {
                SearchResultViewModel(repository) as T
            }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(OnBoardingViewModel::class.java) -> {
                OnBoardingViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this){
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }. also { instance = it }
    }
}