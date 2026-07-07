package com.example.hashscanner.network

import com.example.hashscanner.database.AppDatabase
import java.time.LocalDateTime

class UploadManager(

    private val db: AppDatabase,

    private val uploader: ServerUploader

) {

    suspend fun uploadPending() {

        val dao = db.suspiciousDao()

        val list = dao.getNotSent()

        if (list.isEmpty()) {
            return
        }

        val uploadTime = LocalDateTime.now().toString()

        list.forEach { app ->

            try {

                val success = uploader.upload(app)

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