package com.adamszewera.glovochallenge

import android.app.Application
import android.os.StrictMode
import com.adamszewera.glovochallenge.di.components.AppComponent
import com.adamszewera.glovochallenge.di.components.DaggerAppComponent
import com.adamszewera.glovochallenge.di.modules.AppModule
import timber.log.Timber

class App : Application() {

    private lateinit var appComponent: AppComponent


    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()


        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            setStrictMode()
        }


    }




    private fun setStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectCustomSlowCalls()
                .detectNetwork()
                .penaltyLog()
                .penaltyDeath()
                .build()
        )
    }


    fun getAppComponent(): AppComponent {
        return this.appComponent
    }



    init {
        instance = this
    }

    companion object {

        private var instance: App? = null

        fun getInstance(): App {
            return instance as App
        }

    }





}