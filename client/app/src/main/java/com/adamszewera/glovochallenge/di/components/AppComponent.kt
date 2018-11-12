package com.adamszewera.glovochallenge.di.components

import android.content.Context
import com.adamszewera.glovochallenge.App
import com.adamszewera.glovochallenge.data.GlovoRepository
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

    fun inject(app: App)

    @ApplicationContext
    fun context(): Context

    fun glovoRepository(): GlovoRepository


}