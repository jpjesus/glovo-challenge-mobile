package com.adamszewera.glovochallenge.di.modules

import android.content.Context
import com.adamszewera.glovochallenge.App
import com.adamszewera.glovochallenge.di.qualifiers.ApplicationContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: App) {

    @Provides
    @Singleton
    fun provideApplication(): App {
        return app
    }

    @Provides
    @ApplicationContext
    fun provideContext(): Context {
        return app
    }

}