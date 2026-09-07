package com.example.hashscanner.di

import android.content.Context
import com.example.hashscanner.BuildConfig
import com.example.hashscanner.data.network.ApiService
import com.example.hashscanner.data.network.ConnectivityObserver
import com.example.hashscanner.data.network.NetworkConnectivityObserver
import com.example.hashscanner.utils.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): Interceptor {
        val bodyLogger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val headersLogger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        }

        return Interceptor { chain ->
            if (!BuildConfig.DEBUG) {
                chain.proceed(chain.request())
            } else {
                val isUploadApk = chain.request().url.encodedPath.contains("upload_apk")
                if (isUploadApk) {
                    headersLogger.intercept(chain)
                } else {
                    bodyLogger.intercept(chain)
                }
            }
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(300, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideReportBaseUrl(): String {
        return Constants.BASE_URL
    }


    @Provides
    @Singleton
    fun provideReportRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson,
        baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideConnectivityObserver(@ApplicationContext context: Context): ConnectivityObserver {
        return NetworkConnectivityObserver(context)
    }
}
