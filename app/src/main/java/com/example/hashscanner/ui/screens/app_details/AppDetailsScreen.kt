package com.example.hashscanner.ui.screens.app_details

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.hashscanner.R
import com.example.hashscanner.data.network.NetworkResult
import com.example.hashscanner.ui.theme.BackgroundColor
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.ui.theme.spacing
import com.example.hashscanner.ui.ui_utils.AppTopBar
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

    var isLoading by remember { mutableStateOf(false) }

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
                    databaseViewModel.markApkUploaded(packageName, date)
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
                    databaseViewModel.deleteApp(it)
                    navController.popBackStack()
                }
            }
        }
    )

    // --- UI Layout ---
    Scaffold(
        containerColor = MaterialTheme.colorScheme.BackgroundColor,
        topBar = {
            AppTopBar(
                topBarText = stringResource(R.string.topbar_title_app_details),
                onClick = { navController.popBackStack() }
            )
        },
        bottomBar = {
            appDetails?.let { app ->
                AppDetailsBottomBar(
                    isSystem = app.isSystem,
                    isUploaded = app.apkUploaded,
                    isLoading = isLoading,
                    onUploadApkClicked = {
                        appDetails?.let {
                            scannerViewModel.uploadAPK(
                                apkPath = it.apkPath,
                                packageName = it.packageName,
                                appName = it.appName,
                                sha256 = it.sha256
                            )
                        }
                    },
                    onDeleteClicked = {
                        appDetails?.let { app ->
                            try {
                                if (app.isSystem) {
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
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
                    }
                )
            }
        }
    ) { innerPadding ->
        AppDetailsContent(innerPadding, appDetails)
    }
}

@Composable
private fun AppDetailsContent(
    paddingValues: PaddingValues,
    appInfo: com.example.hashscanner.data.model.db_entities.AppInfo?
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
            item { AppHeaderSection(appInfo = app) }
            item { RiskScoreCard(score = app.riskScore) }
            item { SuspiciousReasonsCard(appInfo = app) }
            item { TechnicalDetailsCard(appInfo = app) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppDetailsScreenPreview() {
    HashScannerTheme {
        AppDetailsContent(
            paddingValues = PaddingValues(16.dp),
            appInfo = null // You could provide sample AppInfo here
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
