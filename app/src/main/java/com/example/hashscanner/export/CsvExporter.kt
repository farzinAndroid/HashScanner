package com.example.hashscanner.export

import android.content.Context
import android.os.Environment
import com.example.hashscanner.database.AppDatabase
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CsvExporter(

    private val context: Context,

    private val db: AppDatabase

) {

    suspend fun exportCsv(): File {

        val apps = db.appDao().getAll()

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
            "Apps_$time.csv"
        )

        val builder = StringBuilder()

        builder.append(
            "App Name,Package,Risk,Score,SHA256\n"
        )

        apps.forEach {

            builder.append(
                "\"${it.appName}\","
            )

            builder.append(
                "\"${it.packageName}\","
            )

            builder.append(
                "\"${it.riskLevel}\","
            )

            builder.append(
                "\"${it.riskScore}\","
            )

            builder.append(
                "\"${it.sha256}\"\n"
            )

        }

        file.writeText(builder.toString())

        return file

    }

}