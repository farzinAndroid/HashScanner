package com.example.hashscanner.ui.screens.scan

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.hashscanner.R
import com.example.hashscanner.ui.theme.BackgroundColor
import com.example.hashscanner.ui.ui_utils.AppTopBar
import com.example.hashscanner.viewmodel.AppDatabaseViewModel
import com.example.hashscanner.viewmodel.ScannerViewModel


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.tooling.preview.Preview
import com.example.hashscanner.ui.theme.HashScannerTheme

@Composable
fun ScanScreen(
    scannerViewModel: ScannerViewModel,
    appDatabaseViewModel: AppDatabaseViewModel,
    navController: NavController
) {
    val isScanCompeted by scannerViewModel.isScanCompleted.collectAsStateWithLifecycle()

    ScanScreenContent(
        isScanCompeted = isScanCompeted,
        onBackClick = { navController.popBackStack() },
        scanningProgressSection = { paddingValues ->
            ScanningProgressSection(
                paddingValues = paddingValues,
                scannerViewModel = scannerViewModel
            )
        },
        scanCompleteSection = { paddingValues ->
            ScanCompleteSection(
                paddingValues = paddingValues,
                navController = navController,
                scannerViewModel = scannerViewModel,
                appDatabaseViewModel = appDatabaseViewModel
            )
        }
    )
}

@Composable
fun ScanScreenContent(
    isScanCompeted: ScanPageState,
    onBackClick: () -> Unit,
    scanningProgressSection: @Composable (PaddingValues) -> Unit,
    scanCompleteSection: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.BackgroundColor,
        topBar = {
            AppTopBar(
                topBarText = if (isScanCompeted == ScanPageState.SCANNING)
                    stringResource(R.string.topbar_title_scan_apps)
                else
                    stringResource(R.string.topbar_title_scan_result),
                onClick = onBackClick
            )
        },
        content = { paddingValues ->
            if (isScanCompeted == ScanPageState.SCANNING || isScanCompeted == ScanPageState.UPLOADING) {
                scanningProgressSection(paddingValues)
            } else {
                scanCompleteSection(paddingValues)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ScanScreenPreview() {
    HashScannerTheme {
        ScanScreenContent(
            isScanCompeted = ScanPageState.SCANNING,
            onBackClick = {},
            scanningProgressSection = { ScanningProgressSectionPreview() },
            scanCompleteSection = { ScanCompleteSectionPreview() }
        )
    }
}

