package com.adamszewera.glovochallenge.ui.home

import android.location.Location
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.adamszewera.glovochallenge.core.viewmodel.BaseViewModel
import com.adamszewera.glovochallenge.data.AppRepository
import com.adamszewera.glovochallenge.data.GlovoRepository
import com.adamszewera.glovochallenge.data.TrackingRepository
import com.adamszewera.glovochallenge.data.models.City
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class HomeViewModel constructor(
    private val appRepository: AppRepository,
    private val glovoRepository: GlovoRepository,
    private val trackingRepository: TrackingRepository
): BaseViewModel() {


    val cities = MutableLiveData<List<City>>()
    val currentCity = MutableLiveData<City>()
    val firstAccess = MutableLiveData<Boolean>()
    val currentLocation  = MutableLiveData<Location>()

    // city values
    val infoCode = ObservableField<String>()
    val infoName = ObservableField<String>()
    val infoCountry = ObservableField<String>()
    val infoCurrency = ObservableField<String>()
//    val infoEnabled = ObservableField<Boolean>()
//    val infoBusy = ObservableField<Boolean>()
    val infoTimeZone = ObservableField<String>()
    val infoLanguage = ObservableField<String>()



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
                { city ->
                    currentCity.value = city


                    Timber.d("current city: %s", city)

                    infoCode.set(city.code)
                    infoName.set(city.name)
                    infoCountry.set(city.country_code)
                    infoCurrency.set(city.currency)
//                    infoEnabled.set(city.enabled)
//                    infoBusy.set(city.busy)
                    infoTimeZone.set(city.time_zone)
                    infoLanguage.set(city.language_code)
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


    /**
     * Starts receiving locations.
     * Note: permissions must be granted first
     */
    fun startLocations() {
        trackingRepository.initTracking()
        val disposable = trackingRepository.getLocations().subscribe(
            {
                currentLocation.value = it
            },
            {
                Timber.e(it)
            }
        )
        disposeOnClear.add(disposable)
    }


}