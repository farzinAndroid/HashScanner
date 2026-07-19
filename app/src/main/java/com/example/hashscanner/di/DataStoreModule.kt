package com.example.hashscanner.di

import android.content.Context
import com.example.hashscanner.data.datastore.DataStoreRepo
import com.example.hashscanner.data.datastore.DataStoreRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideDataStoreRepoImpl(@ApplicationContext context: Context) : DataStoreRepo =
        DataStoreRepoImpl(context)

}