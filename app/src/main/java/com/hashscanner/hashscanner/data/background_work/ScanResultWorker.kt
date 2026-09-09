package com.hashscanner.hashscanner.data.background_work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.hashscanner.hashscanner.R
import com.hashscanner.hashscanner.data.datastore.DataStoreRepo
import com.hashscanner.hashscanner.data.model.api.ScanResultModel
import com.hashscanner.hashscanner.data.model.db_entities.AnalysisStatus
import com.hashscanner.hashscanner.data.model.db_entities.NotificationStage
import com.hashscanner.hashscanner.data.network.NetworkResult
import com.hashscanner.hashscanner.notification.NotificationHelper
import com.hashscanner.hashscanner.repository.AppDatabaseRepo
import com.hashscanner.hashscanner.repository.NetworkRepo
import com.hashscanner.hashscanner.utils.Constants
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class ScanResultWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val appDatabaseRepo: AppDatabaseRepo,
    private val networkRepo: NetworkRepo,
    private val notificationHelper: NotificationHelper,
    private val dataStoreRepo: DataStoreRepo
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        
//        return runTestLogic()
         return runProductionLogic()
    }

    /**
     * TEST LOGIC: 
     * Simulates the background process without touching the database or server.
     * Simply sends a test notification to verify background persistence.
     */
    private fun runTestLogic(): Result {
        val attempt = runAttemptCount + 1
        Log.d(Constants.TAG, "Background TEST Mode: Execution #$attempt")
        
        notificationHelper.showTestNotification(attempt)
        
        // Success tells the Periodic scheduler that this interval is done.
        // It will run again in exactly 15 minutes.
        return Result.success()
    }

    /**
     * PRODUCTION LOGIC:
     * Real implementation that monitors the database for PENDING scans
     * and queries the API for results.
     */
    private suspend fun runProductionLogic(): Result {
        Log.d(Constants.TAG, "Background PRODUCTION Mode: Checking pending scans...")

        // 0. Ensure deviceId is retrieved correctly ( loophole fix for static var reset)
        val deviceId = dataStoreRepo.getString(Constants.DEVICE_ID_DATASTORE_ID) ?: Constants.DEVICE_ID
        if (deviceId.isEmpty()) {
            Log.e(Constants.TAG, "Production: Device ID is empty. Skipping.")
            return Result.failure()
        }

        // 1. Fetch pending IDs
        val pendingScans = appDatabaseRepo.getPendingScans(AnalysisStatus.PENDING.name).first()

        if (pendingScans.isEmpty()) {
            Log.d(Constants.TAG, "Production: No work to do. Stopping.")
            return Result.success()
        }

        // 2. Poll API for each pending scan
        pendingScans.forEach { scan ->
            val model = ScanResultModel(scanId = scan.id, deviceId = deviceId)
            try {
                val response = networkRepo.getScanResult(model)
                if (response is NetworkResult.Success) {
                    val data = response.data ?: return@forEach
                    val currentStage = data.stage // e.g. "INITIAL_READY", "COMPLETE", "WAITING_ADMIN"
                    
                    val uploadedSummary = data.uploadedApks?.summary
                    val isApkReady = uploadedSummary?.complete == true && (uploadedSummary.total > 0)

                    // 1. Check for Initial Results Notification
                    if (data.ready && currentStage == NotificationStage.INITIAL_READY.name && scan.lastNotifiedStage == NotificationStage.NONE.name) {
                        showAndLogNotification(scan.id, data, NotificationStage.INITIAL_READY.name)
                    }
                    
                    // 2. Check for APK Analysis Notification
                    else if (isApkReady && (scan.lastNotifiedStage == NotificationStage.INITIAL_READY.name || scan.lastNotifiedStage == NotificationStage.NONE.name)) {
                        val msg = applicationContext.getString(R.string.notification_apk_analysis_finished)
                        notificationHelper.showScanResultNotification(scan.id, msg)
                        appDatabaseRepo.updateLastNotifiedStage(scan.id, NotificationStage.APK_READY.name)
                    }

                    // 3. Check for Final Complete Notification
                    else if (currentStage == NotificationStage.COMPLETE.name && scan.lastNotifiedStage != NotificationStage.COMPLETE.name) {
                        showAndLogNotification(scan.id, data, NotificationStage.COMPLETE.name)
                    }

                    // 4. Finally, mark as COMPLETED in DB when stage is COMPLETE
                    if (currentStage == NotificationStage.COMPLETE.name) {
                        appDatabaseRepo.updateAnalysisStatus(scan.id, AnalysisStatus.COMPLETED.name)
                    }
                }
            } catch (e: Exception) {
                Log.e(Constants.TAG, "Production: Error checking scan ${scan.id}", e)
            }
        }

        return Result.success()
    }

    private suspend fun showAndLogNotification(scanId: String, data: com.hashscanner.hashscanner.data.model.api.ScanResultResponse, stageName: String) {
        val notificationMessage = data.finalReport?.message 
            ?: data.initialReport?.let { 
                applicationContext.getString(R.string.notification_analysis_finished_summary, it.summary.virus, it.summary.suspicious)
            } ?: data.message ?: applicationContext.getString(R.string.notification_default_finished_message)

        notificationHelper.showScanResultNotification(scanId = scanId, message = notificationMessage)
        appDatabaseRepo.updateLastNotifiedStage(scanId, stageName)
    }
}
