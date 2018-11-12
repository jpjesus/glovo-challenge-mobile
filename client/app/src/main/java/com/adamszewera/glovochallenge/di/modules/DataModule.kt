package com.adamszewera.glovochallenge.di.modules

import com.adamszewera.glovochallenge.data.source.remote.GlovoApi
import com.adamszewera.glovochallenge.data.source.remote.GlovoDataSource
import com.adamszewera.glovochallenge.data.source.remote.GlovoRetrofitDataSource
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




}