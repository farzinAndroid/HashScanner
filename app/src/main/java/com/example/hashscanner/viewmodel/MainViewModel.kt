package com.example.hashscanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hashscanner.data.database.AppDatabase
import com.example.hashscanner.data.database.entity.AppInfo
import kotlinx.coroutines.launch

class MainViewModel(

    private val db: AppDatabase

) : ViewModel() {

    var apps: List<AppInfo> = emptyList()

    fun loadApps(

        onFinish: () -> Unit

    ) {

        viewModelScope.launch {

            apps = db.appDao().getAll()

            onFinish()

        }

    }

}