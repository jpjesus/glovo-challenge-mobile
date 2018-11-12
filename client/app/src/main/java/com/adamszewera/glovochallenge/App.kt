package com.adamszewera.glovochallenge

import android.app.Application
import android.app.ApplicationErrorReport
import com.adamszewera.glovochallenge.data.GlovoRepository
import com.adamszewera.glovochallenge.di.components.AppComponent
import com.adamszewera.glovochallenge.di.components.DaggerAppComponent
import com.adamszewera.glovochallenge.di.modules.AppModule
import timber.log.Timber
import javax.inject.Inject

class App : Application() {

    @Inject
    lateinit var glovoRepository: GlovoRepository

    private val appComponent : AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }


    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        appComponent.inject(this)


        val disposable = glovoRepository.getCountries()
            .subscribe(
                {
                    Timber.d("countries: %s", it.size)
                    Timber.d("countries: %s", it)
                },
                {
                    Timber.e(it)
                }
            )


    }
}