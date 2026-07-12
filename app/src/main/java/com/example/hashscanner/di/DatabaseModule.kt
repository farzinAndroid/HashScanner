package com.example.hashscanner.di

import android.content.Context
import androidx.room.Room
import com.example.hashscanner.data.database.AppDatabase
import com.example.hashscanner.data.database.dao.AppDao
import com.example.hashscanner.data.database.dao.PermissionDao
import com.example.hashscanner.data.database.dao.ScanHistoryDao
import com.example.hashscanner.data.database.dao.SuspiciousDao
import com.example.hashscanner.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context = context,
            name = Constants.APP_DB_NAME,
            klass = AppDatabase::class.java
        )
            .fallbackToDestructiveMigration()
            .build()


    @Provides
    fun provideAppDao(database: AppDatabase): AppDao {
        return database.appDao()
    }

    @Provides
    fun providePermissionDao(database: AppDatabase): PermissionDao {
        return database.permissionDao()
    }

    @Provides
    fun provideSuspiciousDao(database: AppDatabase): SuspiciousDao {
        return database.suspiciousDao()
    }

    @Provides
    fun provideScanHistoryDao(database: AppDatabase): ScanHistoryDao {
        return database.scanHistoryDao()
    }
}