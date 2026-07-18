package com.example.hashscanner.data.export

import android.content.Context
import android.os.Environment
import com.example.hashscanner.data.database.AppDatabase
import com.example.hashscanner.utils.Constants
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
            Constants.DATE_FORMAT_EXPORT,
            Locale.getDefault()
        ).format(Date())

        val file = File(
            dir,
            "Apps_$time.csv"
        )

        val builder = StringBuilder()

        builder.append(
            "${Constants.EXPORT_LABEL_APPLICATION}," +
                    "${Constants.EXPORT_LABEL_PACKAGE}," +
                    "${Constants.EXPORT_LABEL_RISK_LEVEL}," +
                    "${Constants.EXPORT_LABEL_RISK_SCORE}," +
                    "${Constants.EXPORT_LABEL_SHA256}\n"
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