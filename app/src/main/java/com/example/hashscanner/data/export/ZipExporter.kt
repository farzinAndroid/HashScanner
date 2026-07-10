package com.example.hashscanner.data.export

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ZipExporter(

    private val context: Context

) {

    fun createZip(

        files: List<File>

    ): File {

        val outputDir = context.getExternalFilesDir(
            Environment.DIRECTORY_DOCUMENTS
        ) ?: throw Exception("Cannot access Documents folder.")

        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        val time = SimpleDateFormat(
            "yyyy-MM-dd_HH-mm-ss",
            Locale.getDefault()
        ).format(Date())

        val zipFile = File(
            outputDir,
            "SecurityReport_$time.zip"
        )

        ZipOutputStream(
            FileOutputStream(zipFile)
        ).use { zip ->

            files.forEach { file ->

                if (!file.exists())
                    return@forEach

                FileInputStream(file).use { input ->

                    zip.putNextEntry(
                        ZipEntry(file.name)
                    )

                    input.copyTo(zip)

                    zip.closeEntry()

                }

            }

        }

        return zipFile

    }

}