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
    val firstAccess = MutableLiveData<Boolean>()

    // city values
//    android:id="@+id/info_code_tv"
//    android:id="@+id/info_name_tv"
//    android:id="@+id/info_country_code_tv"
//    android:id="@+id/info_currency_tv"
//    android:id="@+id/info_enabled_tv"
//    android:id="@+id/info_busy_tv"
//    android:id="@+id/info_time_zone_tv"
//    android:id="@+id/info_language_code_tv"
//    android:id="@+id/info_working_area_tv"



    fun loadData() {
        val disposable = appRepository.isFirstAccess()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    firstAccess.setValue(it)
                    if (it) {   // first time
                        // todo: set first time to false
                    }

                },
                {
                    // todo: show error
                }
            )
        disposeOnClear.add(disposable)
    }


    fun loadCurrentCity(cityCode: String) {
        val disposable = glovoRepository.getCity(cityCode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    currentCity.setValue(it)
                },
                {
                    Timber.e(it)
                }
            )
        disposeOnClear.add(disposable)
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