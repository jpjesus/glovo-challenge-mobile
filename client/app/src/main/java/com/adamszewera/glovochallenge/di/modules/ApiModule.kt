package com.adamszewera.glovochallenge.di.modules

import android.content.Context
import com.adamszewera.glovochallenge.BuildConfig
import com.adamszewera.glovochallenge.R
import com.adamszewera.glovochallenge.data.source.remote.GlovoApi
import com.adamszewera.glovochallenge.di.qualifiers.ApplicationContext
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApiModule {


    @Singleton
    @Provides
    fun provideGlovoApi(@ApplicationContext context: Context, httpClient: OkHttpClient): GlovoApi {
        val gson = GsonBuilder()
            .create()

        var retrofit = Retrofit.Builder()
            .baseUrl(context.getString(R.string.api_endpoint))
            .client(httpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()


        return retrofit.create(GlovoApi::class.java)
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            // no logging in production
            loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        builder.addInterceptor(loggingInterceptor)
        return builder.build()
    }



}