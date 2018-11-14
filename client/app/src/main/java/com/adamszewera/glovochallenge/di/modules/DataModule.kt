package com.adamszewera.glovochallenge.di.modules

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.adamszewera.glovochallenge.data.AppRepository
import com.adamszewera.glovochallenge.data.TrackingRepository
import com.adamszewera.glovochallenge.data.source.local.PreferencesDataSource
import com.adamszewera.glovochallenge.data.source.remote.GlovoApi
import com.adamszewera.glovochallenge.data.source.remote.GlovoDataSource
import com.adamszewera.glovochallenge.data.source.remote.GlovoRetrofitDataSource
import com.adamszewera.glovochallenge.di.qualifiers.ApplicationContext
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideRemoteGlovoDataSource(glovoApi: GlovoApi): GlovoDataSource {
        return GlovoRetrofitDataSource(glovoApi)
    }


    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    @Singleton
    fun provideAppRepository(preferencesDataSource: PreferencesDataSource): AppRepository {
        return AppRepository(preferencesDataSource)
    }

    @Provides
    @Singleton
    fun provideTrackingRepository(@ApplicationContext context: Context) : TrackingRepository {
        return TrackingRepository(context)
    }

}