package com.example.hashscanner.export

import android.content.Context
import android.os.Environment
import com.example.hashscanner.database.AppDatabase
import com.google.gson.GsonBuilder
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class JsonExporter(

    private val context: Context,

    private val db: AppDatabase

) {

    suspend fun exportJson(): File {

        val apps = db.appDao().getAll()

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
            "yyyy-MM-dd_HH-mm-ss",
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