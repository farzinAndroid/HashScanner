package com.example.hashscanner.network

import com.example.hashscanner.data.database.dao.SuspiciousDao
import com.example.hashscanner.data.datastore.DataStoreRepo
import com.example.hashscanner.utils.Constants
import com.example.hashscanner.utils.DateTimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UploadManager @Inject constructor(

    private val dao: SuspiciousDao,

    private val uploader: ServerUploader,

    private val dataStoreRepo: DataStoreRepo

) {

    suspend fun uploadPending() = withContext(Dispatchers.IO) {

        val list = dao.getNotSent()

        if (list.isEmpty()) {
            return@withContext
        }

        val deviceId = dataStoreRepo.getString(Constants.UUID_DATASTORE_ID) ?: "unknown_device"

        val uploadTime = DateTimeUtils.getCurrentDateTime()

        list.forEach { app ->

            try {

                val success = uploader.upload(app, deviceId)

                if (success) {

                    dao.markUploaded(

                        pkg = app.packageName,

                        date = uploadTime

                    )

                }

            } catch (e: Exception) {

                e.printStackTrace()

            }

        }

    }

}