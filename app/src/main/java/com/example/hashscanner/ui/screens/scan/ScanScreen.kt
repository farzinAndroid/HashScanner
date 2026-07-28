package com.example.hashscanner.ui.screens.scan

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.hashscanner.R
import com.example.hashscanner.data.model.ScanReport
import com.example.hashscanner.ui.theme.BackgroundColor
import com.example.hashscanner.ui.ui_utils.AppTopBar
import com.example.hashscanner.viewmodel.AppDatabaseViewmodel
import com.example.hashscanner.viewmodel.ScannerViewmodel


@Composable
fun ScanScreen(
    scannerViewmodel: ScannerViewmodel = hiltViewModel(),
    navController: NavController
) {


    val isScanCompeted by scannerViewmodel.isScanCompleted.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.BackgroundColor,
        topBar = {
            AppTopBar(
                topBarText = if (isScanCompeted == ScanPageState.SCANNING)
                    stringResource(R.string.topbar_title_scan_apps)
                else
                    stringResource(R.string.topbar_title_scan_result),
                onClick = {
                    navController.popBackStack()
                }
            )
        },
        content = { paddingValues ->

            if (isScanCompeted == ScanPageState.SCANNING || isScanCompeted == ScanPageState.UPLOADING) {
                ScanningProgressSection(
                    paddingValues = paddingValues
                )
            } else {
                ScanCompleteSection(
                    paddingValues = paddingValues,
                    navController = navController
                )
            }

        }
    )
}