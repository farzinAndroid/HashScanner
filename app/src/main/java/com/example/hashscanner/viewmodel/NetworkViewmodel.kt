package com.example.hashscanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hashscanner.repository.AppDatabaseRepo
import com.example.hashscanner.repository.NetworkRepo
import com.example.hashscanner.utils.DateTimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class NetworkViewmodel @Inject constructor(
    private val networkRepo: NetworkRepo,
    private val appDatabaseRepo: AppDatabaseRepo
) : ViewModel() {

    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading.asStateFlow()

    fun uploadPendingReports() = viewModelScope.launch(Dispatchers.IO) {
        networkRepo.uploadPending()
    }

    fun uploadAPK(apkPath: String, packageName: String) = viewModelScope.launch(Dispatchers.IO) {
        _isUploading.value = true
        val file = File(apkPath)
        val success = networkRepo.uploadAPK(file, packageName)
        
        if (success) {
            val date = DateTimeUtils.getCurrentDateTime()
            appDatabaseRepo.markApkUploaded(packageName, date)
        }
        
        _isUploading.value = false
    }
}
