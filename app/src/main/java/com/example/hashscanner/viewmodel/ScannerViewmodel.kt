package com.example.hashscanner.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hashscanner.repository.ScannerRepository
import com.example.hashscanner.ui.screens.scan.ScanPageState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScannerViewmodel @Inject constructor(
    private val scannerRepository: ScannerRepository
) : ViewModel() {

    val totalCount = MutableStateFlow<Int>(0)
    val scannedCount = MutableStateFlow<Int>(0)
    val suspiciousCount = MutableStateFlow<Int>(0)
    val remainingCount = MutableStateFlow<Int>(0)
    val appName = MutableStateFlow<String>("")
    val iconBitmap = MutableStateFlow<Bitmap?>(null)
    val isScanCompleted = MutableStateFlow<ScanPageState>(ScanPageState.SCANNING)

    fun startScan() = viewModelScope.launch(Dispatchers.IO) {
        isScanCompleted.value = ScanPageState.SCANNING
        scannerRepository.startScan(
            onProgress = { scanned, total, suspicious, remaining, app,icon ->
                totalCount.value = total
                scannedCount.value = scanned
                suspiciousCount.value = suspicious
                remainingCount.value = remaining
                appName.value = app
                iconBitmap.value = icon

            }
        )
    }.invokeOnCompletion {
        isScanCompleted.value = ScanPageState.SCAN_COMPLETE
    }
    }
