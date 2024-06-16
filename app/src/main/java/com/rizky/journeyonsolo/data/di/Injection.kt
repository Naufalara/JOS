package com.rizky.journeyonsolo.data.di

import com.rizky.journeyonsolo.data.DestinationRepository
import com.rizky.journeyonsolo.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(): DestinationRepository? {
        val apiService = ApiConfig.getApiService()
        return DestinationRepository.getInstance(apiService)
    }
}