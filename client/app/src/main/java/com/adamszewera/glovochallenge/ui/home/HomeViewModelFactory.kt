package com.adamszewera.glovochallenge.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adamszewera.glovochallenge.data.AppRepository
import com.adamszewera.glovochallenge.data.GlovoRepository
import com.adamszewera.glovochallenge.data.TrackingRepository
import javax.inject.Inject


class HomeViewModelFactory @Inject constructor(
    private val appRepository: AppRepository,
    private val glovoRepository: GlovoRepository,
    private val trackingRepository: TrackingRepository
): ViewModelProvider.NewInstanceFactory() {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(appRepository, glovoRepository, trackingRepository) as T
    }
}