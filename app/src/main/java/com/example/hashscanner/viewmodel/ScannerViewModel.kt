package com.example.hashscanner.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hashscanner.data.model.api.AuthenticationResponse
import com.example.hashscanner.data.model.api.UserAuthentication
import com.example.hashscanner.data.network.NetworkResult
import com.example.hashscanner.repository.AppDatabaseRepo
import com.example.hashscanner.repository.NetworkRepo
import com.example.hashscanner.repository.ScannerRepository
import com.example.hashscanner.ui.screens.scan.ScanPageState
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
class ScannerViewModel @Inject constructor(
    private val scannerRepository: ScannerRepository,
    private val networkRepo: NetworkRepo,
    private val appDatabaseRepo: AppDatabaseRepo
) : ViewModel() {

    // --- Scanning States ---
    val totalCount = MutableStateFlow<Int>(0)
    val scannedCount = MutableStateFlow<Int>(0)
    val suspiciousCount = MutableStateFlow<Int>(0)
    val remainingCount = MutableStateFlow<Int>(0)
    val appName = MutableStateFlow<String>("")
    val iconBitmap = MutableStateFlow<Bitmap?>(null)
    val isScanCompleted = MutableStateFlow<ScanPageState>(ScanPageState.SCANNING)

    // --- Uploading States ---
    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading.asStateFlow()

    fun startScan() = viewModelScope.launch(Dispatchers.IO) {
        isScanCompleted.value = ScanPageState.SCANNING
        scannerRepository.startScan(
            onProgress = { scanned, total, suspicious, remaining, app, icon ->
                totalCount.value = total
                scannedCount.value = scanned
                suspiciousCount.value = suspicious
                remainingCount.value = remaining
                appName.value = app
                iconBitmap.value = icon
            }
        )
        isScanCompleted.value = ScanPageState.UPLOADING
        networkRepo.uploadPending()
        isScanCompleted.value = ScanPageState.SCAN_COMPLETE
    }

    // --- Network / Upload Functions ---
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

    // --- Authentication ---

    private val _authenticationResponse = MutableStateFlow<NetworkResult<AuthenticationResponse>>(NetworkResult.Idle())
    val authenticationResponse = _authenticationResponse.asStateFlow()

    fun authenticate(userAuthentication: UserAuthentication){
        viewModelScope.launch(Dispatchers.IO) {
            _authenticationResponse.emit(NetworkResult.Loading())
            val result = networkRepo.authenticate(userAuthentication)
            _authenticationResponse.emit(result)
        }
    }
}
