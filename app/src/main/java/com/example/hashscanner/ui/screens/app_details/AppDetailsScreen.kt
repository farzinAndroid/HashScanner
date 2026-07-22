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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.hashscanner.R
import com.example.hashscanner.ui.theme.BackgroundColor
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.ui.theme.spacing
import com.example.hashscanner.ui.ui_utils.AppTopBar
import com.example.hashscanner.viewmodel.AppDatabaseViewmodel

@Composable
fun AppDetailsScreen(
    databaseViewmodel: AppDatabaseViewmodel = hiltViewModel(),
    packageName: String,
    navController: NavController
) {

    val context = LocalContext.current
    val appDetails by databaseViewmodel.appByPackage.collectAsStateWithLifecycle()

    val uninstallLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { _ ->
            if (!isPackageInstalled(context, packageName)) {
                appDetails?.let {
                    databaseViewmodel.deleteApp(it)
                    navController.popBackStack()
                }
            }
        }
    )

    LaunchedEffect(packageName) {
        databaseViewmodel.getAppByPackage(packageName)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.BackgroundColor,
        topBar = {
            AppTopBar(
                topBarText = stringResource(R.string.topbar_title_app_details),
                onClick = {
                    navController.popBackStack()
                }
            )
        },
        bottomBar = {
            appDetails?.let { app ->
                    AppDetailsBottomBar(
                        isSystem = app.isSystem,
                        onUploadApkClicked = {
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
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = MaterialTheme.spacing.dp16),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.dp16),
            contentPadding = PaddingValues(vertical = MaterialTheme.spacing.dp16)
        ) {
            appDetails?.let { app ->
                item { AppHeaderSection(appInfo = app) }
                item { RiskScoreCard(score = app.riskScore) }
                item { SuspiciousReasonsCard(appInfo = app) }
                item { TechnicalDetailsCard(appInfo = app) }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppDetailsScreenPreview() {
    HashScannerTheme {
        AppDetailsScreen(
            packageName = "com.example.hashscanner",
            navController = NavController(LocalContext.current)
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
