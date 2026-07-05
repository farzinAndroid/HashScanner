package com.example.hashscanner.network

import com.example.hashscanner.database.AppDatabase

class UploadManager(

    private val db: AppDatabase,

    private val uploader: ServerUploader

) {

    suspend fun uploadPending() {

        val dao = db.suspiciousDao()

        val list = dao.getNotSent()

        if (list.isEmpty())
            return

        val ok = uploader.upload(list)

        if (ok) {

            list.forEach {

                dao.markUploaded(

                    it.packageName

                )

            }

        }

    }

}