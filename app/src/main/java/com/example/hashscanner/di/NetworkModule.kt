package com.example.hashscanner.di

import com.example.hashscanner.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
}
