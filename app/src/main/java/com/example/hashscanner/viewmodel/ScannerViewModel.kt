package com.example.hashscanner.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.hashscanner.R
import com.example.hashscanner.data.model.api.ApkUploadResponse
import com.example.hashscanner.data.model.api.AuthenticationResponse
import com.example.hashscanner.data.model.api.ScanResultModel
import com.example.hashscanner.data.model.api.ScanResultResponse
import com.example.hashscanner.data.model.api.UserAuthentication
import com.example.hashscanner.data.model.db_entities.AnalysisStatus
import com.example.hashscanner.data.model.db_entities.ScanHistory
import com.example.hashscanner.data.network.NetworkResult
import com.example.hashscanner.notification.NotificationHelper
import com.example.hashscanner.repository.AppDatabaseRepo
import com.example.hashscanner.repository.NetworkRepo
import com.example.hashscanner.repository.ScannerRepository
import com.example.hashscanner.ui.screens.scan.ScanPageState
import com.example.hashscanner.utils.Constants
import com.example.hashscanner.utils.DateTimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    private val appDatabaseRepo: AppDatabaseRepo,
    private val workManager: WorkManager,
    private val scanResultWorkRequest: PeriodicWorkRequest,
    private val notificationHelper: NotificationHelper,
    @ApplicationContext private val context: Context
) : ViewModel() {

    init {
        // Every time the ViewModel is created (App starts), 
        // we check if there are any old pending scans from previous sessions.
        startBackgroundResultCheck()
    }

    // --- Scanning States ---
    val totalCount = MutableStateFlow<Int>(0)
    val scannedCount = MutableStateFlow<Int>(0)
    val suspiciousCount = MutableStateFlow<Int>(0)
    val remainingCount = MutableStateFlow<Int>(0)
    val appName = MutableStateFlow<String>("")
    val iconBitmap = MutableStateFlow<Bitmap?>(null)
    val isScanCompleted = MutableStateFlow<ScanPageState>(ScanPageState.SCANNING)
    val currentScanId = MutableStateFlow<String?>(null)
    private var scanJob: Job? = null

    fun resetScanState() {
        cancelScanJob()
        clearScanResult() // Resets UI popup state
        isScanCompleted.value = ScanPageState.SCANNING
        totalCount.value = 0
        scannedCount.value = 0
        suspiciousCount.value = 0
        remainingCount.value = 0
        appName.value = ""
        iconBitmap.value = null
    }


    fun cancelScanJob(){
        scanJob?.cancel()
    }

    fun startScan() {
        if (scanJob?.isActive == true) return
        scanJob = viewModelScope.launch(Dispatchers.IO) {
            val startTime = System.currentTimeMillis()
            val scanId = UUID.randomUUID().toString()
            currentScanId.value = scanId
            isScanCompleted.value = ScanPageState.SCANNING
            
            // Runs the local package scanning process
            scannerRepository.startScan(
                scanId = scanId,
                onProgress = { scanned, total, suspicious, remaining, app, icon ->
                    totalCount.value = total
                    scannedCount.value = scanned
                    suspiciousCount.value = suspicious
                    remainingCount.value = remaining
                    appName.value = app
                    iconBitmap.value = icon
                }
            )

            if (!isActive) return@launch // Guard for cancellation

            isScanCompleted.value = ScanPageState.UPLOADING
            networkRepo.uploadPending(scanId) // Uploads local findings to server
            networkRepo.scanFinished(ScanResultModel(scanId = scanId, deviceId = Constants.DEVICE_ID)) // Notifies server scan is done

            if (!isActive) return@launch

            // Persists the scan snapshot to the database
            val endTime = System.currentTimeMillis()
            val history = ScanHistory(
                id = scanId,
                scanDate = DateTimeUtils.getCurrentDate(),
                scanTime = DateTimeUtils.getCurrentTime(),
                totalApps = totalCount.value,
                scannedApps = scannedCount.value,
                
                systemApps = appDatabaseRepo.countSystemAppsByScanId(scanId).first(),
                userApps = appDatabaseRepo.countUserAppsByScanId(scanId).first(),
                
                safeApps = appDatabaseRepo.countSafeAppsByScanId(scanId).first(),
                lowRisk = appDatabaseRepo.countLowRiskAppsByScanId(scanId).first(),
                mediumRisk = appDatabaseRepo.countMediumRiskAppsByScanId(scanId).first(),
                highRisk = appDatabaseRepo.countHighRiskAppsByScanId(scanId).first(),
                criticalRisk = appDatabaseRepo.countCriticalAppsByScanId(scanId).first(),
                
                safeUserApps = appDatabaseRepo.countSafeAppsByScanId(scanId, onlyUser = true).first(),
                lowRiskUserApps = appDatabaseRepo.countLowRiskAppsByScanId(scanId, onlyUser = true).first(),
                mediumRiskUserApps = appDatabaseRepo.countMediumRiskAppsByScanId(scanId, onlyUser = true).first(),
                highRiskUserApps = appDatabaseRepo.countHighRiskAppsByScanId(scanId, onlyUser = true).first(),
                criticalRiskUserApps = appDatabaseRepo.countCriticalAppsByScanId(scanId, onlyUser = true).first(),
                
                duration = endTime - startTime,
                analysisStatus = AnalysisStatus.PENDING.name // Stores as String using Enum name
            )
            appDatabaseRepo.insertScanHistory(history)
            
            startBackgroundResultCheck()

            isScanCompleted.value = ScanPageState.SCAN_COMPLETE
        }
    }

    private val _scanResultResponseResponse =
        MutableStateFlow<NetworkResult<ScanResultResponse>>(NetworkResult.Idle())
    val scanResultResponse = _scanResultResponseResponse.asStateFlow()

    // Separate flow for the Global Notification popup
    private val _scanResultNotificationPopUp =
        MutableStateFlow<NetworkResult<ScanResultResponse>>(NetworkResult.Idle())
    val scanResultNotificationPopUp = _scanResultNotificationPopUp.asStateFlow()

    private var job: Job? = null
    private var currentPollingScanId: String? = null

    /**
     * Entry point for requesting scan results.
     * If scanId is provided, it polls for that specific scan (e.g., in History Details).
     * Otherwise, it enters global monitoring mode to check for any unfinished scans.
     */
    fun getScanResult(scanId: String? = null) {
        viewModelScope.launch {
            if (scanId != null) {
                startContextualPolling(scanId)
            } else {
                startGlobalPendingMonitoring()
            }
        }
    }

    /**
     * Starts a targeted polling job for a specific scanId.
     */
    private fun startContextualPolling(scanId: String) {
        // Prevents restarting the same job if already running
        if (job?.isActive == true && currentPollingScanId == scanId) return
        stopPolling() // Cancel any existing global or other contextual jobs
        currentPollingScanId = scanId
        
        job = viewModelScope.launch(Dispatchers.IO) {
            val scanResultModel = ScanResultModel(scanId = scanId, deviceId = Constants.DEVICE_ID)
            while (isActive) {
                Log.d(Constants.TAG, "Contextual Polling: $scanId")
                handlePollingResult(scanId, scanResultModel)
                delay(3000L.milliseconds)
            }
        }
    }

    /**
     * Periodically monitors the database for any scans marked as PENDING.
     * It checks all pending scans in a single loop every 10 seconds.
     */
    private fun startGlobalPendingMonitoring() {
        if (job?.isActive == true && currentPollingScanId == null) return
        stopPolling()
        currentPollingScanId = null // Signals global mode

        job = viewModelScope.launch(Dispatchers.IO) {
            // High-level loop that persists as long as we are in global mode
            while (isActive && currentPollingScanId == null) {
                // 1. Fetch the absolute latest list of pending scans from the DB
                val pendingScans = appDatabaseRepo.getPendingScans(AnalysisStatus.PENDING.name).first()
                
                if (pendingScans.isEmpty()) {
                    Log.d(Constants.TAG, "Global Monitor: No pending scans. Waiting...")
                } else {
                    Log.d(Constants.TAG, "Global Monitor: Checking ${pendingScans.size} scans")
                    pendingScans.forEach { scan ->
                        val model = ScanResultModel(scanId = scan.id, deviceId = Constants.DEVICE_ID)
                        handlePollingResult(scan.id, model)
                    }
                }

                // 2. Wait for the next tick
                delay(5000L.milliseconds)
            }
        }
    }


    /**
     * Common logic to handle API response. Updates DB and UI state if result is ready.
     */
    private suspend fun handlePollingResult(scanId: String, model: ScanResultModel): Boolean {
        try {
            val result = networkRepo.getScanResult(model)
            if (result is NetworkResult.Success && result.data?.ready == true) {
                Log.d(Constants.TAG, "Result Ready for $scanId (Stage: ${result.data.stage})")

                // 1. Check for Full Completion
                // According to backend docs: Only mark as COMPLETED in DB if the finalReport is complete.
                // This ensures WorkManager/Global Polling continues if we are still waiting for deep analysis.
                val isFinalReportComplete = result.data.finalReport?.complete == true
                if (isFinalReportComplete) {
                    appDatabaseRepo.updateAnalysisStatus(scanId, AnalysisStatus.COMPLETED.name)
                }

                // 2. Extract message for notification
                val notificationMessage = result.data.finalReport?.message 
                    ?: result.data.initialReport?.let { 
                        context.getString(R.string.notification_analysis_finished_summary, it.summary.virus, it.summary.suspicious)
                    } ?: result.data.message ?: context.getString(R.string.notification_default_finished_message)

                // 3. Show notification
                notificationHelper.showScanResultNotification(
                    scanId = scanId,
                    message = notificationMessage
                )

                // 4. Emit to data flow (stays visible on screen)
                _scanResultResponseResponse.emit(result)

                // 5. Emit to notification flow (shows global popup)
                _scanResultNotificationPopUp.emit(result)

                return true
            }
        } catch (e: Exception) {
            Log.e(Constants.TAG, "Polling Error for $scanId", e)
        }
        return false
    }

    fun clearScanResult() {
        // Only clear the notification flow so the popup disappears,
        // but the data stays visible in HistoryDetailsScreen.
        _scanResultNotificationPopUp.value = NetworkResult.Idle<ScanResultResponse>()
    }

    fun stopPolling() {
        job?.cancel()
        currentPollingScanId = null
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

    /**
     * Enqueues a WorkManager task to check for pending results when the app is closed.
     * Uses ExistingWorkPolicy.REPLACE to ensure we only have one background monitor running.
     */
    private fun startBackgroundResultCheck() {
        workManager.enqueueUniquePeriodicWork(
            Constants.WORK_TAG_SCAN_RESULT,
            ExistingPeriodicWorkPolicy.REPLACE,
            scanResultWorkRequest
        )
    }


}
