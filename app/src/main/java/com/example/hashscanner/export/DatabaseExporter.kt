package com.example.hashscanner.export

import android.content.Context
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DatabaseExporter(

    private val context: Context

) {

    fun exportDatabase(): File {

        val dbFile = context.getDatabasePath(
            "app_hash_scanner.db"
        )

        if (!dbFile.exists()) {
            throw Exception("Database not found.")
        }

        val outputDir =
            context.getExternalFilesDir(
                Environment.DIRECTORY_DOCUMENTS
            ) ?: throw Exception("Cannot access Documents folder.")

        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw Exception("Cannot create output directory.")
        }

        val time =
            SimpleDateFormat(
                "yyyy-MM-dd_HH-mm-ss",
                Locale.getDefault()
            ).format(Date())

        val backupFile = File(
            outputDir,
            "AppHashScanner_$time.db"
        )

        try {

            dbFile.copyTo(
                backupFile,
                overwrite = true
            )

        } catch (e: Exception) {

            throw Exception(
                "Database export failed.",
                e
            )

        }

        return backupFile

    }

}