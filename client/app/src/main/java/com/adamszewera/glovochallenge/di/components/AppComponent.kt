package com.adamszewera.glovochallenge.di.components

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.adamszewera.glovochallenge.App
import com.adamszewera.glovochallenge.core.ui.BaseFragment
import com.adamszewera.glovochallenge.data.AppRepository
import com.adamszewera.glovochallenge.data.GlovoRepository
import com.adamszewera.glovochallenge.data.TrackingRepository
import com.adamszewera.glovochallenge.di.modules.ViewModelModule
import com.adamszewera.glovochallenge.di.modules.ApiModule
import com.adamszewera.glovochallenge.di.modules.AppModule
import com.adamszewera.glovochallenge.di.modules.DataModule
import com.adamszewera.glovochallenge.di.qualifiers.ApplicationContext
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ApiModule::class,
        DataModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {

        fun appModule(appModule: AppModule): Builder

        fun build(): AppComponent
    }

    @ApplicationContext
    fun context(): Context

    fun glovoRepository(): GlovoRepository

    fun appRepository(): AppRepository

    fun trackingRepository(): TrackingRepository


    fun inject(fragment: BaseFragment)
}