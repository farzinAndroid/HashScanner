package com.hashscanner.hashscanner.repository

import android.util.Log
import com.hashscanner.hashscanner.data.database.dao.SuspiciousDao
import com.hashscanner.hashscanner.data.datastore.DataStoreRepo
import com.hashscanner.hashscanner.data.model.api.ApkUploadResponse
import com.hashscanner.hashscanner.data.model.api.AppReport
import com.hashscanner.hashscanner.data.model.api.AuthenticationResponse
import com.hashscanner.hashscanner.data.model.api.ScanResultModel
import com.hashscanner.hashscanner.data.model.api.ScanResultResponse
import com.hashscanner.hashscanner.data.model.api.UserAuthentication
import com.hashscanner.hashscanner.data.network.ApiService
import com.hashscanner.hashscanner.data.network.BaseApiResponse
import com.hashscanner.hashscanner.data.network.NetworkResult
import com.hashscanner.hashscanner.data.network.ProgressRequestBody
import com.hashscanner.hashscanner.data.network.ScanFinishedResponse
import com.hashscanner.hashscanner.utils.Constants
import com.hashscanner.hashscanner.utils.DateTimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class NetworkRepo @Inject constructor(
    private val apiService: ApiService,
    private val dao: SuspiciousDao,
    private val dataStoreRepo: DataStoreRepo
) : BaseApiResponse() {

    suspend fun uploadPending(scanId: String) = withContext(Dispatchers.IO) {
        val list = dao.getNotSent().first()
        if (list.isEmpty()) return@withContext

        val deviceId = dataStoreRepo.getString(Constants.DEVICE_ID_DATASTORE_ID) ?: "unknown_device"
        val uploadTime = DateTimeUtils.getCurrentDateTime()

        val report = AppReport.fromSuspiciousApps(
            list = list,
            deviceId = deviceId,
            scanId = scanId
        )

        val result = safeApiCall { apiService.sendScanReport(report) }

        if (result is NetworkResult.Success) {
            list.forEach { app ->
                dao.markReportSent(
                    pkg = app.packageName,
                    date = uploadTime
                )
            }
            Log.d(Constants.TAG, "Successfully uploaded batch report for ${list.size} apps. ScanId: $scanId")
        } else if (result is NetworkResult.Error) {
            Log.e(Constants.TAG, "Failed to upload batch report: ${result.message}")
        }
    }

    suspend fun upload(appReport: AppReport): NetworkResult<Unit> {
        return safeApiCall { apiService.sendScanReport(appReport) }
    }

    suspend fun uploadAPK(
        apk: File,
        packageName: String,
        scanId: String,
        deviceId: String,
        appName: String,
        sha256: String,
        onProgress: ((percentage: Int) -> Unit)? = null
    ): NetworkResult<ApkUploadResponse> {
        val contentType = "application/vnd.android.package-archive".toMediaTypeOrNull()
        val requestBody = if (onProgress != null) {
            ProgressRequestBody(
                file = apk,
                contentType = contentType,
                onProgress = { _, _, percentage ->
                    onProgress(percentage)
                }
            )
        } else {
            apk.asRequestBody(contentType)
        }

        val apkPart = MultipartBody.Part.createFormData("apk", apk.name, requestBody)
        val packagePart = packageName.toRequestBody("text/plain".toMediaTypeOrNull())
        val scanIdPart = scanId.toRequestBody("text/plain".toMediaTypeOrNull())
        val deviceIdPart = deviceId.toRequestBody("text/plain".toMediaTypeOrNull())
        val appNamePart = appName.toRequestBody("text/plain".toMediaTypeOrNull())
        val sha256Part = sha256.toRequestBody("text/plain".toMediaTypeOrNull())

        return safeApiCall {
            apiService.uploadApk(
                packageName = packagePart,
                scanId = scanIdPart,
                deviceId = deviceIdPart,
                appName = appNamePart,
                sha256 = sha256Part,
                apk = apkPart
            )
        }
    }


    suspend fun authenticate(userAuthentication: UserAuthentication) : NetworkResult<AuthenticationResponse>{
        return safeApiCall { apiService.authenticate(userAuthentication) }
    }



    suspend fun getScanResult(scanResultModel: ScanResultModel) : NetworkResult<ScanResultResponse>{
        return safeApiCall { apiService.getScanResult(scanResultModel) }
    }


    suspend fun scanFinished(scanResultModel: ScanResultModel) : NetworkResult<ScanFinishedResponse>{
        return safeApiCall { apiService.scanFinished(scanResultModel) }
    }



}
