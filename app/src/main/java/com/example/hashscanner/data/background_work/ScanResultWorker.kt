package com.example.hashscanner.data.background_work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.example.hashscanner.R
import com.example.hashscanner.data.model.api.ScanResultModel
import com.example.hashscanner.data.model.db_entities.AnalysisStatus
import com.example.hashscanner.data.network.NetworkResult
import com.example.hashscanner.notification.NotificationHelper
import com.example.hashscanner.repository.AppDatabaseRepo
import com.example.hashscanner.repository.NetworkRepo
import com.example.hashscanner.utils.Constants
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class ScanResultWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val appDatabaseRepo: AppDatabaseRepo,
    private val networkRepo: NetworkRepo,
    private val notificationHelper: NotificationHelper
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

        // 1. Fetch pending IDs
        val pendingScans = appDatabaseRepo.getPendingScans(AnalysisStatus.PENDING.name).first()

        if (pendingScans.isEmpty()) {
            Log.d(Constants.TAG, "Production: No work to do. Stopping.")
            return Result.success()
        }

        // 2. Poll API for each pending scan
        pendingScans.forEach { scan ->
            val model = ScanResultModel(scanId = scan.id, deviceId = Constants.DEVICE_ID)
            try {
                val response = networkRepo.getScanResult(model)
                if (response is NetworkResult.Success && response.data?.ready == true) {
                    val notificationMessage = response.data.finalReport?.message 
                        ?: response.data.initialReport?.let { 
                            applicationContext.getString(R.string.notification_analysis_finished_summary, it.summary.virus, it.summary.suspicious)
                        } ?: applicationContext.getString(R.string.notification_default_finished_message)

                    // 1. Show notification FIRST
                    notificationHelper.showScanResultNotification(
                        scanId = scan.id,
                        message = notificationMessage
                    )

                    // 2. Mark as COMPLETED in DB so we stop monitoring it
                    appDatabaseRepo.updateAnalysisStatus(scan.id, AnalysisStatus.COMPLETED.name)
                }
            } catch (e: Exception) {
                Log.e(Constants.TAG, "Production: Error checking scan ${scan.id}", e)
            }
        }

        // We return success because even if some are still pending, 
        // the Periodic scheduler will wake us up again in 15 minutes automatically.
        return Result.success()
    }
}
