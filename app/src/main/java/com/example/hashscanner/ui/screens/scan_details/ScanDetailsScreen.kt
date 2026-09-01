package com.example.hashscanner.ui.screens.scan_details

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hashscanner.R
import com.example.hashscanner.data.model.api.ApiAppResult
import com.example.hashscanner.data.model.api.FinalReport
import com.example.hashscanner.data.model.api.FinalSummary
import com.example.hashscanner.data.model.api.InitialReport
import com.example.hashscanner.data.model.api.ReportSummary
import com.example.hashscanner.data.model.api.ScanResultResponse
import com.example.hashscanner.data.model.api.UploadSummary
import com.example.hashscanner.data.model.api.UploadedApks
import com.example.hashscanner.data.model.db_entities.AnalysisStatus
import com.example.hashscanner.data.model.db_entities.AppInfo
import com.example.hashscanner.data.model.db_entities.NotificationStage
import com.example.hashscanner.data.model.db_entities.ScanHistory
import com.example.hashscanner.data.network.NetworkResult
import com.example.hashscanner.ui.navigation.Screens
import com.example.hashscanner.ui.theme.AccentPurpleColor
import com.example.hashscanner.ui.theme.BackgroundColor
import com.example.hashscanner.ui.theme.BlackWhiteColor
import com.example.hashscanner.ui.theme.GreenColor
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.ui.theme.RedColor
import com.example.hashscanner.ui.ui_utils.AppTopBar
import com.example.hashscanner.ui.ui_utils.RiskLevelsUI
import com.example.hashscanner.utils.Constants
import com.example.hashscanner.utils.DateTimeUtils
import com.example.hashscanner.utils.PackageUtils
import com.example.hashscanner.viewmodel.AppDatabaseViewModel
import com.example.hashscanner.viewmodel.ScannerViewModel

@Composable
fun ScanDetailsScreen(
    navController: NavController,
    scanId: String?,
    databaseViewModel: AppDatabaseViewModel,
    scannerViewModel: ScannerViewModel
) {
    val scanHistoryList by databaseViewModel.scanHistory.collectAsStateWithLifecycle(emptyList())
    val scanResult by scannerViewModel.scanResultResponse.collectAsStateWithLifecycle()
    val allAppsFromDB by databaseViewModel.allApps.collectAsStateWithLifecycle()
    val apkUploadResponse by scannerViewModel.apkUploadResponse.collectAsStateWithLifecycle()
    var isLoading by remember { mutableStateOf(false) }
    val scanDetails = remember(scanHistoryList, scanId) {
        scanHistoryList.find { it.id == scanId }
    }

    var appDetailsByClickUpload by remember { mutableStateOf<AppInfo?>(null) }
    var appToDelete by remember { mutableStateOf<AppInfo?>(null) }

    val context = LocalContext.current

    val uninstallLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { _ ->
            appToDelete?.let { app ->
                if (!PackageUtils.isPackageInstalled(context, app.packageName)) {
                    databaseViewModel.markAsDeleted(app.packageName, app.scanId)
                    Toast.makeText(context, R.string.app_uninstalled_successfully, Toast.LENGTH_SHORT).show()
                    databaseViewModel.getAllApps(app.scanId)
                }
            }
        }
    )


    val data = (scanResult as? NetworkResult.Success)?.data
    val isReady = data?.ready == true
    val stage = data?.stage ?: ""
    val isComplete = stage == "COMPLETE"



    LaunchedEffect(scanId) {
        if (scanId != null) {
            scannerViewModel.getScanResult(scanId)
            databaseViewModel.getAllApps(scanId)
        }
    }

    LaunchedEffect(apkUploadResponse) {
        when (val result = apkUploadResponse) {
            is NetworkResult.Success -> {
                isLoading = false
                if (result.data?.success == false) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.toast_upload_error),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val date = DateTimeUtils.getCurrentDateTime()
                    databaseViewModel.markApkUploaded(appDetailsByClickUpload?.sha256 ?: "", date)


                    // This wakes up the background polling logic to wait for the final admin check or apk check.
                    if (scanId != null) {
                        databaseViewModel.updateAnalysisStatus(scanId, AnalysisStatus.PENDING.name)
                        databaseViewModel.updateLastNotifiedStage(scanId, NotificationStage.INITIAL_READY.name)
                    }

                    Toast.makeText(
                        context,
                        context.getString(R.string.toast_upload_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    if (scanId != null) {
                        databaseViewModel.getAppByPackage(appDetailsByClickUpload?.packageName ?: "", scanId)
                    }
                }
            }

            is NetworkResult.Error -> {
                isLoading = false
                Toast.makeText(
                    context,
                    result.message ?: context.getString(R.string.toast_upload_error),
                    Toast.LENGTH_LONG
                ).show()
                Log.e(Constants.TAG,result.message.toString())
            }

            is NetworkResult.Loading -> {
                isLoading = true
            }

            is NetworkResult.Idle -> {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                topBarText = stringResource(R.string.topbar_title_scan_result),
                onClick = { navController.popBackStack() }
            )
        },
        containerColor = MaterialTheme.colorScheme.BackgroundColor
    ) { paddingValues ->
        if (scanDetails == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.AccentPurpleColor)
            }
        } else {
            ScanDetailsContent(
                paddingValues = paddingValues,
                scanDetails = scanDetails,
                scanResult = scanResult,
                navController = navController,
                onRiskLevelClick = { riskLevel ->
                    navController.navigate(Screens.AppList(riskLevel, scanId))
                },
                data = data,
                isReady = isReady,
                stage = stage,
                isComplete = isComplete,
                allAppsFromDB = allAppsFromDB,
                onApkUploadClicked = { app->
                    appDetailsByClickUpload = app
                    scannerViewModel.uploadAPK(
                        apkPath = app.apkPath,
                        packageName = app.packageName,
                        appName = app.appName,
                        sha256 = app.sha256,
                        scanId = app.scanId
                    )
                },
                onApkDeleteClicked = { app ->
                    appToDelete = app
                    try {
                        if (app.isSystem) {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                this.data = Uri.parse("package:${app.packageName}")
                            }
                            context.startActivity(intent)
                        } else {
                            val intent = Intent(Intent.ACTION_DELETE).apply {
                                this.data = Uri.parse("package:${app.packageName}")
                            }
                            uninstallLauncher.launch(intent)
                        }
                    } catch (_: Exception) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.error_action_not_supported),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }
    }
}

@Composable
fun ScanDetailsContent(
    paddingValues: PaddingValues,
    scanDetails: ScanHistory,
    scanResult: NetworkResult<ScanResultResponse>,
    navController: NavController,
    onRiskLevelClick: (RiskLevelsUI) -> Unit,
    data: ScanResultResponse?,
    isReady: Boolean,
    stage: String,
    isComplete: Boolean,
    allAppsFromDB: List<AppInfo>,
    onApkUploadClicked: (app: AppInfo) -> Unit,
    onApkDeleteClicked: (app: AppInfo) -> Unit
) {
    val totalSuspicious = scanDetails.highRisk + scanDetails.criticalRisk
    val resultColor =
        if (totalSuspicious > 0) MaterialTheme.colorScheme.RedColor else MaterialTheme.colorScheme.GreenColor

    val dbAppMap = remember(allAppsFromDB) {
        // Primary index by hash, secondary by package
        val byPkg = allAppsFromDB.associateBy { it.packageName }
        val byHash = allAppsFromDB.associateBy { it.sha256 }
        Pair(byHash, byPkg)
    }


    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // --- Source of Truth Indicator ---
        item(span = { GridItemSpan(2) }) {
            SourceOfTruthChip(isReady = isReady)
        }

        // --- Header Section (Persistent Layer) ---
        item(span = { GridItemSpan(2) }) {
            ScanDetailHeader(
                scan = scanDetails,
                serverSusAppCount = data?.initialReport?.summary?.suspicious,
                serverVirusAppCount = data?.initialReport?.summary?.virus
            )
        }

        // --- API Result Section (Dynamic Layer) ---
        item(span = { GridItemSpan(2) }) {
            if (data != null) {
                if (isReady) {
                    val initialReport = data.initialReport
                    val finalReport = data.finalReport

                    // Show Final Summary if Complete, otherwise Initial
                    if (isComplete && finalReport?.complete == true) {
                        ApiSummaryCard(
                            result = if (finalReport.summary.virus > 0) "VIRUS" else "CLEAN",
                            message = finalReport.message
                                ?: stringResource(R.string.notification_analysis_complete_generic)
                        )
                    } else if (initialReport != null) {
                        ApiSummaryCard(
                            result = if (initialReport.summary.virus > 0) "VIRUS" else "CLEAN",
                            message = stringResource(
                                R.string.report_initial_summary,
                                initialReport.summary.virus,
                                initialReport.summary.suspicious
                            )
                        )
                    }
                } else {
                    ApiProgressCard(
                        message = data.message ?: stringResource(R.string.report_cloud_analyzing),
                        total = data.totalReports ?: 0,
                        checked = data.checkedReports ?: 0,
                        pending = data.pendingReports ?: 0
                    )
                }
            } else {
                // Initial "Local Only" State
                ApiProgressCard(
                    message = stringResource(R.string.status_checking),
                    total = 0,
                    checked = 0,
                    pending = 0
                )
            }
        }

        // --- Uploaded APKs Progress (Conditional Layer) ---
        data?.uploadedApks?.let { uploaded ->
            if (!uploaded.summary.complete && (uploaded.summary.total > 0)) {
                item(span = { GridItemSpan(2) }) {
                    UploadedApkProgressCard(
                        total = uploaded.summary.total,
                        checked = uploaded.summary.checked,
                        pending = uploaded.summary.pending
                    )
                }
            }
        }

        // --- Verified App List (Conditional Layer) ---
        if (isReady && data?.initialReport?.apps?.isNotEmpty() == true) {
            item(span = { GridItemSpan(2) }) {
                Text(
                    text = stringResource(R.string.api_analysis_results_title),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.BlackWhiteColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            items(data.initialReport.apps, span = { GridItemSpan(2) }) { apiApp ->
                val matchingDbApp = dbAppMap.first[apiApp.sha256] ?: dbAppMap.second[apiApp.packageName]
                ApiAppResultCard(
                    apiAppResult = apiApp,
                    matchingDbApp = matchingDbApp,
                    scanId = scanDetails.id,
                    onUploadClicked = {app->
                        onApkUploadClicked(app)
                    },
                    onDeleteClicked = { app ->
                        onApkDeleteClicked(app)
                    },
                    onCardClicked = { packageName, scanId ->
                        navController.navigate(Screens.AppDetails(packageName, scanId))
                    }
                )
            }
        }

        // Show that the final results are still coming
        if (isReady && !isComplete) {
            item(span = { GridItemSpan(2) }) {
                Row(
                    modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(14.dp),
                        strokeWidth = 1.5.dp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.report_finalizing),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }



        // ---. App List Entry ---
        item(span = { GridItemSpan(2) }) {
            ScanDetailAppListEntry(
                serverSusAppCount = data?.initialReport?.summary?.suspicious,
                serverVirusAppCount = data?.initialReport?.summary?.virus,
                localSusAppCount = scanDetails.lowRisk + scanDetails.mediumRisk + scanDetails.highRisk + scanDetails.criticalRisk
            ) {
                navController.navigate(Screens.RiskLevelList(scanId = scanDetails.id))
            }
        }

        // --- Metadata Section (Persistent Layer) ---
        item(span = { GridItemSpan(2) }) {
            ScanDetailDataSection(
                scan = scanDetails,
                serverSafeAppCount = data?.initialReport?.summary?.safe,
                serverVirusAppCount = data?.initialReport?.summary?.virus,
                serverSusAppCount = data?.initialReport?.summary?.suspicious
            )
        }
    }
}


// --- Previews ---

private val mockScan = ScanHistory(
    id = "1",
    scanDate = "1402/05/30",
    scanTime = "12:30",
    totalApps = 150,
    scannedApps = 150,
    systemApps = 130,
    userApps = 20,
    safeApps = 140,
    lowRisk = 5,
    mediumRisk = 3,
    highRisk = 2,
    criticalRisk = 0,
    safeUserApps = 10,
    lowRiskUserApps = 5,
    mediumRiskUserApps = 3,
    highRiskUserApps = 2,
    criticalRiskUserApps = 0,
    duration = 4500L
)

@Preview(showBackground = true, name = "1. Local State")
@Composable
fun LocalStatePreview() {
    HashScannerTheme {
        ScanDetailsContent(
            paddingValues = PaddingValues(0.dp),
            scanDetails = mockScan,
            scanResult = NetworkResult.Idle(),
            navController = rememberNavController(),
            onRiskLevelClick = {},
            data = ScanResultResponse(ready = false),
            isReady = false,
            stage = "",
            isComplete = false,
            allAppsFromDB = emptyList(),
            onApkUploadClicked = {},
            onApkDeleteClicked = {}
        )
    }
}

@Preview(showBackground = true, name = "2. Initial Ready State")
@Composable
fun InitialStatePreview() {
    HashScannerTheme {
        val initialData = ScanResultResponse(
            ready = true,
            stage = "INITIAL_READY",
            initialReport = InitialReport(
                complete = true,
                summary = ReportSummary(
                    15,
                    0,
                    0,
                    15,
                    emptyList(),
                    emptyList(),
                    emptyList()
                ),
                apps = listOf(
                    ApiAppResult(
                        "Google",
                        "com.android.chrome",
                        "",
                        20,
                        "LOW",
                        "",
                        "SAFE",
                        "NONE",
                        "این برنامه بررسی شد و سالم است."
                    )
                )
            )
        )
        ScanDetailsContent(
            paddingValues = PaddingValues(0.dp),
            scanDetails = mockScan,
            scanResult = NetworkResult.Success(
                message = "Success",
                data = initialData
            ),
            navController = rememberNavController(),
            onRiskLevelClick = {},
            data = initialData,
            isReady = true,
            stage = "INITIAL_READY",
            isComplete = false,
            allAppsFromDB = emptyList(),
            onApkUploadClicked = {},
            onApkDeleteClicked = {}
        )
    }
}

@Preview(showBackground = true, name = "3. APK Analyzing State")
@Composable
fun ApkStatePreview() {
    HashScannerTheme {
        val apkData = ScanResultResponse(
            ready = true,
            stage = "INITIAL_READY",
            uploadedApks = UploadedApks(
                summary = UploadSummary(
                    5,
                    2,
                    3,
                    0,
                    2,
                    0,
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    false
                ),
                apps = emptyList()
            ),
            initialReport = InitialReport(
                complete = true,
                summary = ReportSummary(
                    15,
                    0,
                    1,
                    14,
                    emptyList(),
                    emptyList(),
                    emptyList()
                ),
                apps = emptyList()
            )
        )
        ScanDetailsContent(
            paddingValues = PaddingValues(0.dp),
            scanDetails = mockScan,
            scanResult = NetworkResult.Success(
                message = "Success",
                data = apkData
            ),
            navController = rememberNavController(),
            onRiskLevelClick = {},
            data = apkData,
            isReady = true,
            stage = "INITIAL_READY",
            isComplete = false,
            allAppsFromDB = emptyList(),
            onApkUploadClicked = {},
            onApkDeleteClicked = {}
        )
    }
}

@Preview(showBackground = true, name = "4. Complete State")
@Composable
fun CompleteStatePreview() {
    HashScannerTheme {
        val completeData = ScanResultResponse(
            ready = true,
            stage = "COMPLETE",
            finalReport = FinalReport(
                complete = true,
                summary = FinalSummary(15, 0, 15, 0, emptyList(), emptyList(), emptyList()),
                message = "تمامی بررسی‌ها با موفقیت به پایان رسید."
            )
        )
        ScanDetailsContent(
            paddingValues = PaddingValues(0.dp),
            scanDetails = mockScan,
            scanResult = NetworkResult.Success(
                message = "Success",
                data = completeData
            ),
            navController = rememberNavController(),
            onRiskLevelClick = {},
            data = completeData,
            isReady = true,
            stage = "COMPLETE",
            isComplete = true,
            allAppsFromDB = emptyList(),
            onApkUploadClicked = {},
            onApkDeleteClicked = {}
        )
    }
}
