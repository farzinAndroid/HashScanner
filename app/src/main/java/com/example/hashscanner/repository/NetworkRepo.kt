package com.example.hashscanner.repository

import com.example.hashscanner.network.ServerUploader
import com.example.hashscanner.network.UploadManager
import javax.inject.Inject

class NetworkRepo @Inject constructor(
    private val uploadManager: UploadManager
) {

    suspend fun uploadPending() = uploadManager.uploadPending()
}