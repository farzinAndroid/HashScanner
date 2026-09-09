package com.hashscanner.hashscanner.data.export

import android.content.Context
import android.os.Environment
import com.hashscanner.hashscanner.data.database.AppDatabase
import com.hashscanner.hashscanner.utils.Constants
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.first
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class JsonExporter(

    private val context: Context,

    private val db: AppDatabase

) {

    suspend fun exportJson(scanId: String): File {

        val apps = db.appDao().getAllByScanId(scanId).first()

        val gson = GsonBuilder()
            .setPrettyPrinting()
            .create()

        val json = gson.toJson(apps)

        val dir = context.getExternalFilesDir(
            Environment.DIRECTORY_DOCUMENTS
        ) ?: throw Exception("Cannot access Documents folder.")

        if (!dir.exists()) {
            dir.mkdirs()
        }

        val time = SimpleDateFormat(
            Constants.DATE_FORMAT_EXPORT,
            Locale.getDefault()
        ).format(Date())

        val file = File(
            dir,
            "Apps_$time.json"
        )

        file.writeText(json)

        return file

    }

}