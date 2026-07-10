package com.example.hashscanner.data.report

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.example.hashscanner.data.database.AppDatabase
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

        // StaticLayout requires a TextPaint object instead of a standard Paint
        val textPaint = TextPaint(paint)

        // Configuration variables
        val pageWidth = 595
        val pageHeight = 842
        val marginX = 20f
        val maxTextWidth = (pageWidth - (marginX * 2)).toInt()

        // Mutable state variables for pagination
        var pageNumber = 1
        var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
        var page = pdf.startPage(pageInfo)
        var canvas = page.canvas
        var y = 40f

        // Helper 1: Checks if we need a new page before drawing
        fun checkPageBreak(requiredHeight: Float) {
            if (y + requiredHeight > pageHeight - 40f) { // 40f bottom margin
                pdf.finishPage(page) // Close current page

                pageNumber++ // Set up next page
                pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
                page = pdf.startPage(pageInfo)
                canvas = page.canvas
                y = 40f // Reset Y to the top of the new page
            }
        }

        // Helper 2: Wraps text and draws it
        fun drawWrappedText(text: String, isTitle: Boolean = false) {
            val currentPaint = if (isTitle) TextPaint(titlePaint) else textPaint

            val staticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                StaticLayout.Builder.obtain(text, 0, text.length, currentPaint, maxTextWidth).build()
            } else {
                @Suppress("DEPRECATION")
                StaticLayout(text, currentPaint, maxTextWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false)
            }

            // Ensure we have enough room for this block of text
            checkPageBreak(staticLayout.height.toFloat())

            canvas.save()
            canvas.translate(marginX, y)
            staticLayout.draw(canvas)
            canvas.restore()

            // Move Y down by the height of the printed text + padding
            y += staticLayout.height + (if (isTitle) 35f else 18f)
        }

        // --- Begin Drawing Document ---

        val dao = db.appDao()
        val apps = dao.getAll()

        drawWrappedText("Android Application Security Report", isTitle = true)
        drawWrappedText("Scan Date : ${LocalDate.now()}")
        drawWrappedText("Scan Time : ${LocalTime.now()}")
        drawWrappedText("Total Apps : ${apps.size}")
        drawWrappedText("Suspicious Apps : ${dao.countSuspiciousApps()}")

        y += 12f // Extra padding before the list

        for (app in dao.getAppsByRisk()) {

            // Draw a separator line
            checkPageBreak(10f)
            canvas.drawLine(marginX, y, pageWidth - marginX, y, paint)
            y += 18f

            drawWrappedText("Application : ${app.appName}")
            drawWrappedText("Package : ${app.packageName}")
            drawWrappedText("Version : ${app.versionName}")
            drawWrappedText("SHA256 : ${app.sha256}")
            drawWrappedText("Certificate SHA256 : ${app.certificateSha256}")
            drawWrappedText("Installer : ${app.installer}")
            drawWrappedText("Target SDK : ${app.targetSdk}")
            drawWrappedText("Risk Score : ${app.riskScore}")
            drawWrappedText("Risk Level : ${app.riskLevel}")
            drawWrappedText("Reasons : ${app.riskReasons}")

            y += 12f // Extra padding between apps
        }

        pdf.finishPage(page)

        // --- Save File ---

        val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!
        if (!dir.exists()) {
            dir.mkdirs()
        }

        val file = File(dir, "SecurityReport.pdf")
        pdf.writeTo(FileOutputStream(file))
        pdf.close()

        return file
    }
}