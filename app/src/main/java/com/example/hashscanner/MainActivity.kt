package com.example.hashscanner

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.compose.rememberNavController
import com.example.hashscanner.ui.navigation.NavGraph
import com.example.hashscanner.ui.theme.BackgroundColor
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.ui.ui_utils.ChangeStatusBarAndNavigationBarColor
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            HashScannerTheme {
                val navController = rememberNavController()


                ChangeStatusBarAndNavigationBarColor(
                    context = this,
                    isDarkMode = isSystemInDarkTheme()
                )

                CompositionLocalProvider(LocalLayoutDirection.provides(LayoutDirection.Rtl)){
                    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .navigationBarsPadding()
                            .statusBarsPadding(),
                        containerColor = MaterialTheme.colorScheme.BackgroundColor
                    ) { innerPadding ->

                        NavGraph(navController)

                        /*Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {


                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp),
                                enabled = isEnabled,
                                onClick = {
                                    isEnabled = false
                                    status = "Scanning installed applications... This may take a while."

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
                                                try {
                                                    pdfFile = PdfGenerator(
                                                        this@MainActivity,
                                                        db
                                                    ).generatePdf()
                                                } catch (e: Exception) {
                                                    errors.add("PDF failed: ${e.message}")
                                                }

                                                try {
                                                    jsonFile =
                                                        JsonExporter(this@MainActivity, db).exportJson()
                                                } catch (e: Exception) {
                                                    errors.add("JSON failed: ${e.message}")
                                                }

                                                try {
                                                    csvFile =
                                                        CsvExporter(this@MainActivity, db).exportCsv()
                                                } catch (e: Exception) {
                                                    errors.add("CSV failed: ${e.message}")
                                                }

                                                try {
                                                    dbFile =
                                                        DatabaseExporter(this@MainActivity).exportDatabase()
                                                } catch (e: Exception) {
                                                    errors.add("Database export failed: ${e.message}")
                                                }

                                                // Bundle the successful files into a ZIP
                                                try {
                                                    // listOfNotNull will safely ignore any files that failed to generate
                                                    val filesToZip = listOfNotNull(
                                                        pdfFile,
                                                        jsonFile,
                                                        csvFile,
                                                        dbFile
                                                    )
                                                    if (filesToZip.isNotEmpty()) {
                                                        zipFile =
                                                            ZipExporter(this@MainActivity).createZip(
                                                                filesToZip
                                                            )
                                                    }
                                                } catch (e: Exception) {
                                                    errors.add("ZIP creation failed: ${e.message}")
                                                }

                                                // Finally, return the data class containing everything
                                                ExportResult(
                                                    pdfFile,
                                                    jsonFile,
                                                    csvFile,
                                                    dbFile,
                                                    zipFile,
                                                    errors
                                                )
                                            }

                                            // 2. Fetch the total app count
                                            val totalApps = withContext(Dispatchers.IO) {
                                                db.appDao()
                                                    .count() // Assuming you have a count() function in your DAO
                                            }

                                            // 3. Update the UI
                                            status = """
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
                                            status = """
                    Critical Scan Failure

                    ${e.message ?: "Unknown Error"}
                """.trimIndent()
                                        } finally {
                                            isEnabled = true
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Red
                                ),

                                ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = button,
                                    )
                                }

                            }

                            Text(
                                text = status,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .verticalScroll(rememberScrollState())
                            )
                        }*/
                    }
                }

            }
        }
    }
}