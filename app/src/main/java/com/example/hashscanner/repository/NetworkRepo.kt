package com.example.hashscanner.repository

import com.example.hashscanner.network.ApkUploader
import com.example.hashscanner.network.UploadManager
import java.io.File
import javax.inject.Inject

class NetworkRepo @Inject constructor(
    private val uploadManager: UploadManager,
    private val apkUploader: ApkUploader
) {

    suspend fun uploadPending() = uploadManager.uploadPending()
    suspend fun uploadAPK(apk: File, packageName: String) = apkUploader.upload(apk, packageName)
}