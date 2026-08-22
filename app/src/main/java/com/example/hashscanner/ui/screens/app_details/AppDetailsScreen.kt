package com.example.hashscanner.ui.screens.app_details

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.hashscanner.R
import com.example.hashscanner.data.model.db_entities.AnalysisStatus
import com.example.hashscanner.data.model.db_entities.NotificationStage
import com.example.hashscanner.data.network.NetworkResult
import com.example.hashscanner.ui.theme.*
import com.example.hashscanner.ui.ui_utils.AppTopBar
import com.example.hashscanner.utils.Constants
import com.example.hashscanner.utils.DateTimeUtils
import com.example.hashscanner.viewmodel.AppDatabaseViewModel
import com.example.hashscanner.viewmodel.ScannerViewModel

@Composable
fun AppDetailsScreen(
    databaseViewModel: AppDatabaseViewModel,
    scannerViewModel: ScannerViewModel,
    packageName: String,
    scanId: String? = null,
    navController: NavController
) {
    val context = LocalContext.current
    val lastScan by databaseViewModel.lastScan.collectAsStateWithLifecycle(initialValue = null)
    val currentScanId = scanId ?: lastScan?.id

    val appDetails by databaseViewModel.appByPackage.collectAsStateWithLifecycle()
    val apkUploadResponse by scannerViewModel.apkUploadResponse.collectAsStateWithLifecycle()
    val apiResponse by scannerViewModel.scanResultResponse.collectAsStateWithLifecycle()

    var isLoading by remember { mutableStateOf(false) }

    // Find this specific app in the API results to get the recommended action
    val apiAppResult = remember(apiResponse, appDetails) {
        if (apiResponse is NetworkResult.Success) {
            val data = (apiResponse as NetworkResult.Success).data
            val allApiApps = (data?.initialReport?.apps ?: emptyList()) +
                    (data?.uploadedApks?.apps ?: emptyList())
            allApiApps.find { it.packageName == appDetails?.packageName }
        } else null
    }

    DisposableEffect(Unit) {
        onDispose {
            scannerViewModel.cancelUpload()
        }
    }

    // --- Side Effects ---
    LaunchedEffect(packageName, currentScanId) {
        if (currentScanId != null) {
            databaseViewModel.getAppByPackage(packageName, currentScanId)
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
                    databaseViewModel.markApkUploaded(appDetails?.sha256 ?: "", date)
                    

                    // This wakes up the background polling logic to wait for the final admin check or apk check.
                    if (currentScanId != null) {
                        databaseViewModel.updateAnalysisStatus(currentScanId, AnalysisStatus.PENDING.name)
                        databaseViewModel.updateLastNotifiedStage(currentScanId, NotificationStage.INITIAL_READY.name)
                    }

                    Toast.makeText(
                        context,
                        context.getString(R.string.toast_upload_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    if (currentScanId != null) {
                        databaseViewModel.getAppByPackage(packageName, currentScanId)
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

    // --- Launchers ---
    val uninstallLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { _ ->
            if (!isPackageInstalled(context, packageName)) {
                appDetails?.let {
                    databaseViewModel.markAsDeleted(it.packageName, it.scanId)
                    Toast.makeText(context, R.string.app_uninstalled_successfully, Toast.LENGTH_SHORT).show()
                    // Refetch data to update UI instantly
                    databaseViewModel.getAppByPackage(packageName, it.scanId)
                }
            }
        }
    )

    // --- UI Layout ---
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    Scaffold(
        containerColor = MaterialTheme.colorScheme.BackgroundColor,
        modifier = Modifier
            .navigationBarsPadding(),
        topBar = {
            AppTopBar(
                topBarText = stringResource(R.string.topbar_title_app_details),
                onClick = { navController.popBackStack() }
            )
        },
        bottomBar = {
            appDetails?.let { app ->
                if (!app.isDeleted) {
                    AppDetailsBottomBar(
                        isSystem = app.isSystem,
                        isUploaded = app.apkUploaded,
                        isLoading = isLoading,
                        isDeleted = app.isDeleted,
                        apiAction = apiAppResult?.action,
                        onUploadApkClicked = {
                            scannerViewModel.uploadAPK(
                                apkPath = app.apkPath,
                                packageName = app.packageName,
                                appName = app.appName,
                                sha256 = app.sha256,
                                scanId = app.scanId
                            )
                        },
                        onDeleteClicked = {
                            try {
                                if (app.isSystem) {
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                        data = Uri.parse("package:$packageName")
                                    }
                                    context.startActivity(intent)
                                } else {
                                    val intent = Intent(Intent.ACTION_DELETE).apply {
                                        data = Uri.parse("package:$packageName")
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
    ) { innerPadding ->
        AppDetailsContent(
            paddingValues = innerPadding,
            appInfo = appDetails,
            apiAction = apiAppResult?.action,
            apiMessage = apiAppResult?.message
        )
    }
}

@Composable
private fun AppDetailsContent(
    paddingValues: PaddingValues,
    appInfo: com.example.hashscanner.data.model.db_entities.AppInfo?,
    apiAction: String? = null,
    apiMessage: String? = null
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = MaterialTheme.spacing.dp16),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.dp16),
        contentPadding = PaddingValues(vertical = MaterialTheme.spacing.dp16)
    ) {
        appInfo?.let { app ->
            item {
                if (app.isDeleted) {
                    ResolvedThreatBanner()
                }
            }
            item { AppHeaderSection(appInfo = app) }
            item { RiskScoreCard(score = app.riskScore) }
            item { SuspiciousReasonsCard(appInfo = app) }
            item {
                TechnicalDetailsCard(
                    appInfo = app,
                    apiAction = apiAction,
                    apiMessage = apiMessage
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AppDetailsScreenPreview() {
    HashScannerTheme {
        AppDetailsContent(
            paddingValues = PaddingValues(16.dp),
            appInfo = null
        )
    }
}

private fun isPackageInstalled(context: Context, packageName: String): Boolean {
    return try {
        context.packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}
