package com.example.hashscanner.report

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import com.example.hashscanner.database.AppDatabase
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.LocalTime

class PdfGenerator(

    private val context: Context,

    private val db: AppDatabase

) {

    suspend fun generatePdf(): File {

        val pdf = PdfDocument()

        val paint = Paint().apply {

            textSize = 14f

            isAntiAlias = true

        }

        val titlePaint = Paint().apply {

            textSize = 20f

            isFakeBoldText = true

            isAntiAlias = true

        }

        val pageInfo = PdfDocument.PageInfo.Builder(

            595,

            842,

            1

        ).create()

        val page = pdf.startPage(pageInfo)

        val canvas = page.canvas

        var y = 40

        val dao = db.appDao()

        val apps = dao.getAll()

        canvas.drawText(

            "Android Application Security Report",

            20f,

            y.toFloat(),

            titlePaint

        )

        y += 35

        canvas.drawText(

            "Scan Date : ${LocalDate.now()}",

            20f,

            y.toFloat(),

            paint

        )

        y += 20

        canvas.drawText(

            "Scan Time : ${LocalTime.now()}",

            20f,

            y.toFloat(),

            paint

        )

        y += 20

        canvas.drawText(

            "Total Apps : ${apps.size}",

            20f,

            y.toFloat(),

            paint

        )

        y += 20

        canvas.drawText(

            "Suspicious Apps : ${dao.countSuspiciousApps()}",

            20f,

            y.toFloat(),

            paint

        )

        y += 30

        for (app in dao.getAppsByRisk()) {

            if (y > 760) break

            canvas.drawText(

                "Application : ${app.appName}",

                20f,

                y.toFloat(),

                paint

            )

            y += 18

            canvas.drawText(

                "Package : ${app.packageName}",

                20f,

                y.toFloat(),

                paint

            )

            y += 18

            canvas.drawText(

                "Version : ${app.versionName}",

                20f,

                y.toFloat(),

                paint

            )

            y += 18

            canvas.drawText(

                "SHA256 : ${app.sha256}",

                20f,

                y.toFloat(),

                paint

            )

            y += 18

            canvas.drawText(

                "Certificate SHA256 : ${app.certificateSha256}",

                20f,

                y.toFloat(),

                paint

            )

            y += 18

            canvas.drawText(

                "Installer : ${app.installer}",

                20f,

                y.toFloat(),

                paint

            )

            y += 18

            canvas.drawText(

                "Target SDK : ${app.targetSdk}",

                20f,

                y.toFloat(),

                paint

            )

            y += 18

            canvas.drawText(

                "Risk Score : ${app.riskScore}",

                20f,

                y.toFloat(),

                paint

            )

            y += 18

            canvas.drawText(

                "Risk Level : ${app.riskLevel}",

                20f,

                y.toFloat(),

                paint

            )

            y += 18

            canvas.drawText(

                "Reasons : ${app.riskReasons}",

                20f,

                y.toFloat(),

                paint

            )

            y += 30

        }

        pdf.finishPage(page)

        val dir = context.getExternalFilesDir(

            Environment.DIRECTORY_DOCUMENTS

        )!!


        if (!dir.exists())

            dir.mkdirs()

        val file = File(

            dir,

            "SecurityReport.pdf"

        )

        pdf.writeTo(

            FileOutputStream(file)

        )

        pdf.close()

        return file

    }

}