package com.adamszewera.glovochallenge.ui.home

import androidx.lifecycle.MutableLiveData
import com.adamszewera.glovochallenge.core.viewmodel.BaseViewModel
import com.adamszewera.glovochallenge.data.AppRepository
import com.adamszewera.glovochallenge.data.GlovoRepository
import com.adamszewera.glovochallenge.data.models.City
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class HomeViewModel constructor(
    private val appRepository: AppRepository,
    private val glovoRepository: GlovoRepository
): BaseViewModel() {


    val cities = MutableLiveData<List<City>>()

    val currentCity = MutableLiveData<City>()



    fun loadData() {
        val disposable = appRepository.isFirstAccess()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (it) {   // first time
                        // todo: set first time to false
                    }

                    // todo: ask for permission
                },
                {
                    // todo: show error
                }
            )
        disposeOnClear.add(disposable)
    }


    fun setCurrentCity() {

    }


    fun loadCities() {
        val disposable = glovoRepository.getCities()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    cities.setValue(it)
                },
                {
                    Timber.e(it)
                }
            )
        disposeOnClear.add(disposable)
    }


    fun searchCity() {

    }



}