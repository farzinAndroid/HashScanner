package com.example.hashscanner

import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.hashscanner.database.AppDatabase
import com.example.hashscanner.export.CsvExporter
import com.example.hashscanner.export.DatabaseExporter
import com.example.hashscanner.export.JsonExporter
import com.example.hashscanner.export.ZipExporter
import com.example.hashscanner.model.ExportResult
import com.example.hashscanner.report.PdfGenerator
import com.example.hashscanner.scanner.PackageScanner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var status: TextView

    private lateinit var button: Button

    private lateinit var db: AppDatabase

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)




        status = findViewById(R.id.status)

        button = findViewById(R.id.start_scan)

        db = AppDatabase.getDatabase(this)

        status.text = "Ready"

        button.setOnClickListener {
            button.isEnabled = false
            status.text = "Scanning installed applications... This may take a while."

            lifecycleScope.launch {
                try {
                    // 1. Run the heavy lifting on the IO thread
                    val exports = withContext(Dispatchers.IO) {

                        val errors = mutableListOf<String>()
                        var pdfFile: File? = null
                        var jsonFile: File? = null
                        var csvFile: File? = null
                        var dbFile: File? = null
                        var zipFile: File? = null

                        // Run the Master Scanner
                        try {
                            val scanner = PackageScanner(this@MainActivity, db)
                            scanner.startScan()
                        } catch (e: Exception) {
                            errors.add("Scan failed: ${e.message}")
                        }

                        // Run individual exporters, catching errors so one failure doesn't crash the rest
                        try { pdfFile = PdfGenerator(this@MainActivity, db).generatePdf() }
                        catch (e: Exception) { errors.add("PDF failed: ${e.message}") }

                        try { jsonFile = JsonExporter(this@MainActivity, db).exportJson() }
                        catch (e: Exception) { errors.add("JSON failed: ${e.message}") }

                        try { csvFile = CsvExporter(this@MainActivity, db).exportCsv() }
                        catch (e: Exception) { errors.add("CSV failed: ${e.message}") }

                        try { dbFile = DatabaseExporter(this@MainActivity).exportDatabase() }
                        catch (e: Exception) { errors.add("Database export failed: ${e.message}") }

                        // Bundle the successful files into a ZIP
                        try {
                            // listOfNotNull will safely ignore any files that failed to generate
                            val filesToZip = listOfNotNull(pdfFile, jsonFile, csvFile, dbFile)
                            if (filesToZip.isNotEmpty()) {
                                zipFile = ZipExporter(this@MainActivity).createZip(filesToZip)
                            }
                        } catch (e: Exception) {
                            errors.add("ZIP creation failed: ${e.message}")
                        }

                        // Finally, return the data class containing everything
                        ExportResult(pdfFile, jsonFile, csvFile, dbFile, zipFile, errors)
                    }

                    // 2. Fetch the total app count
                    val totalApps = withContext(Dispatchers.IO) {
                        db.appDao().count() // Assuming you have a count() function in your DAO
                    }

                    // 3. Update the UI
                    status.text = """
                Scan Completed Successfully

                Applications : $totalApps

                PDF :
                ${exports.pdf?.name ?: "-"}

                JSON :
                ${exports.json?.name ?: "-"}

                CSV :
                ${exports.csv?.name ?: "-"}

                Database :
                ${exports.database?.name ?: "-"}

                ZIP :
                ${exports.zip?.name ?: "-"}

                Saved In :
                ${getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.absolutePath ?: "-"}

                Errors :
                ${if (exports.errors.isEmpty()) "None" else exports.errors.joinToString("\n")}
            """.trimIndent()

                } catch (e: Exception) {
                    e.printStackTrace()
                    status.text = """
                Critical Scan Failure
                
                ${e.message ?: "Unknown Error"}
            """.trimIndent()
                } finally {
                    button.isEnabled = true
                }
            }
        }

    }

}