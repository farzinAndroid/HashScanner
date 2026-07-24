package com.example.hashscanner.di

import android.content.Context
import com.example.hashscanner.data.network.ConnectivityObserver
import com.example.hashscanner.data.network.NetworkConnectivityObserver
import com.example.hashscanner.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Named("report_base_url")
    fun provideReportBaseUrl(): String {
        return Constants.REPORT_BASE_URL
    }

    @Provides
    @Named("apk_base_url")
    fun provideApkBaseUrl(): String {
        return Constants.APK_BASE_URL
    }

    @Provides
    @Singleton
    fun provideConnectivityObserver(@ApplicationContext context: Context): ConnectivityObserver {
        return NetworkConnectivityObserver(context)
    }
}
