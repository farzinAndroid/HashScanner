package com.example.hashscanner.repository

import android.util.Log
import com.example.hashscanner.data.database.dao.SuspiciousDao
import com.example.hashscanner.data.datastore.DataStoreRepo
import com.example.hashscanner.data.model.api.AppReport
import com.example.hashscanner.data.network.ApiService
import com.example.hashscanner.data.network.ApkUploader
import com.example.hashscanner.data.network.BaseApiResponse
import com.example.hashscanner.data.network.NetworkResult
import com.example.hashscanner.utils.Constants
import com.example.hashscanner.utils.DateTimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class NetworkRepo @Inject constructor(
    private val apiService: ApiService,
    private val apkUploader: ApkUploader,
    private val dao: SuspiciousDao,
    private val dataStoreRepo: DataStoreRepo
) : BaseApiResponse() {

    suspend fun uploadPending() = withContext(Dispatchers.IO) {
        val list = dao.getNotSent()
        if (list.isEmpty()) return@withContext

        val deviceId = dataStoreRepo.getString(Constants.UUID_DATASTORE_ID) ?: "unknown_device"
        val uploadTime = DateTimeUtils.getCurrentDateTime()

        list.forEach { app ->
            val report = AppReport.fromSuspiciousApp(app, deviceId)
            val result = safeApiCall { apiService.sendScanReport(report) }

            if (result is NetworkResult.Success) {
                dao.markReportSent(
                    pkg = app.packageName,
                    date = uploadTime
                )
                Log.d("NetworkRepo", "Successfully uploaded report for: ${app.packageName}")
            } else if (result is NetworkResult.Error) {
                Log.e("NetworkRepo", "Failed to upload report for ${app.packageName}: ${result.message}")
            }
        }
    }

    suspend fun upload(appReport: AppReport): NetworkResult<Unit> {
        return safeApiCall { apiService.sendScanReport(appReport) }
    }

    suspend fun uploadAPK(apk: File, packageName: String) = apkUploader.upload(apk, packageName)
}
