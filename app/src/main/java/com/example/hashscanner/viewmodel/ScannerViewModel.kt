package com.example.hashscanner.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hashscanner.data.model.api.ApkUploadResponse
import com.example.hashscanner.data.model.api.AuthenticationResponse
import com.example.hashscanner.data.model.api.ScanResultModel
import com.example.hashscanner.data.model.api.ScanResultResponse
import com.example.hashscanner.data.model.api.UserAuthentication
import com.example.hashscanner.data.model.db_entities.ScanHistory
import com.example.hashscanner.data.network.NetworkResult
import com.example.hashscanner.repository.AppDatabaseRepo
import com.example.hashscanner.repository.NetworkRepo
import com.example.hashscanner.repository.ScannerRepository
import com.example.hashscanner.ui.screens.scan.ScanPageState
import com.example.hashscanner.utils.Constants
import com.example.hashscanner.utils.DateTimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

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
    private var scanJob: Job? = null

    fun resetScanState() {
        scanJob?.cancel()
        isScanCompleted.value = ScanPageState.SCANNING
        totalCount.value = 0
        scannedCount.value = 0
        suspiciousCount.value = 0
        remainingCount.value = 0
        appName.value = ""
        iconBitmap.value = null
    }

    fun startScan() {
        if (scanJob?.isActive == true) return
        scanJob = viewModelScope.launch(Dispatchers.IO) {
            val startTime = System.currentTimeMillis()
            val scanId = UUID.randomUUID().toString()
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
            networkRepo.uploadPending(scanId)
            networkRepo.scanFinished(ScanResultModel(scanId = scanId, deviceId = Constants.DEVICE_ID))

            // Save Scan History
            val endTime = System.currentTimeMillis()
            val history = ScanHistory(
                id = scanId,
                scanDate = DateTimeUtils.getCurrentDate(),
                scanTime = DateTimeUtils.getCurrentTime(),
                totalApps = totalCount.value,
                scannedApps = scannedCount.value,
                safeApps = appDatabaseRepo.countSafeApps().first(),
                lowRisk = appDatabaseRepo.countLowRiskApps().first(),
                mediumRisk = appDatabaseRepo.countMediumRiskApps().first(),
                highRisk = appDatabaseRepo.countHighRiskApps().first(),
                criticalRisk = appDatabaseRepo.countCriticalApps().first(),
                duration = endTime - startTime
            )
            appDatabaseRepo.insertScanHistory(history)

            isScanCompleted.value = ScanPageState.SCAN_COMPLETE
        }
    }


    private val _scanResultResponseResponse =
        MutableStateFlow<NetworkResult<ScanResultResponse>>(NetworkResult.Idle())
    val scanResultResponse = _scanResultResponseResponse.asStateFlow()

    private var job: Job? = null

    fun getScanResult() {

        if (job?.isActive == true) return

        job = viewModelScope.launch(Dispatchers.IO) {

            val lastScanId = appDatabaseRepo.lastScan.first()?.id ?: return@launch

            if (_scanResultResponseResponse.value !is NetworkResult.Success) {
                _scanResultResponseResponse.emit(NetworkResult.Loading())
            }

            val scanResultModel = ScanResultModel(scanId = lastScanId, deviceId = Constants.DEVICE_ID)

            while (isActive) {
                Log.d("ScannerViewModel", "Polling API for scanId: $lastScanId")
                try {
                    val result = networkRepo.getScanResult(scanResultModel)

                    when (val scanResult = result) {
                        is NetworkResult.Error<ScanResultResponse> -> {
                            Log.e("ScannerViewModel", "Polling Error: ${scanResult.message}")
                        }

                        is NetworkResult.Idle<ScanResultResponse> -> {}
                        is NetworkResult.Loading<ScanResultResponse> -> {}
                        is NetworkResult.Success<ScanResultResponse> -> {
                            if (scanResult.data?.ready == true) {
                                Log.d("ScannerViewModel", "Poll Successful: Ready!")
                                _scanResultResponseResponse.emit(
                                    NetworkResult.Success(
                                        scanResult.message.toString(),
                                        scanResult.data
                                    )
                                )
                                stopPolling()
                                break
                            }
                        }
                    }

                } catch (e: Exception) {
                    Log.e("ScannerViewModel", "Polling Exception", e)
                }

                delay(5000L.milliseconds)
            }
        }
    }

    fun stopPolling() {
        job?.cancel()
    }


    // --- Network / Upload Functions ---
    fun uploadPendingReports(scanId: String) = viewModelScope.launch(Dispatchers.IO) {
        networkRepo.uploadPending(scanId)
    }


    private val _apkUploadResponse =
        MutableStateFlow<NetworkResult<ApkUploadResponse>>(NetworkResult.Idle())
    val apkUploadResponse = _apkUploadResponse.asStateFlow()

    private var uploadJob: Job? = null

    fun uploadAPK(
        apkPath: String,
        packageName: String,
        appName: String,
        sha256: String
    ) {
        uploadJob?.cancel()
        uploadJob = viewModelScope.launch(Dispatchers.IO) {
            _apkUploadResponse.emit(NetworkResult.Loading())

            val lastScanId = appDatabaseRepo.lastScan.first()?.id ?: "unknown_scan"
            val file = File(apkPath)
            val result = networkRepo.uploadAPK(
                apk = file,
                packageName = packageName,
                scanId = lastScanId,
                deviceId = Constants.DEVICE_ID,
                appName = appName,
                sha256 = sha256
            )

            _apkUploadResponse.emit(result)
        }
    }

    fun cancelUpload() {
        uploadJob?.cancel()
        _apkUploadResponse.value = NetworkResult.Idle()
    }

    // --- Authentication ---

    private val _authenticationResponse =
        MutableStateFlow<NetworkResult<AuthenticationResponse>>(NetworkResult.Idle())
    val authenticationResponse = _authenticationResponse.asStateFlow()

    fun authenticate(userAuthentication: UserAuthentication) {
        viewModelScope.launch(Dispatchers.IO) {
            _authenticationResponse.emit(NetworkResult.Loading())
            val result = networkRepo.authenticate(userAuthentication)
            _authenticationResponse.emit(result)
        }
    }


}
