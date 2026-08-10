package com.example.hashscanner.ui.screens.scan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.hashscanner.ui.navigation.Screens
import com.example.hashscanner.ui.theme.BackgroundColor
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.viewmodel.AppDatabaseViewModel
import com.example.hashscanner.viewmodel.ScannerViewModel

@Composable
fun ScanCompleteSection(
    paddingValues: PaddingValues,
    scannerViewModel: ScannerViewModel,
    appDatabaseViewModel: AppDatabaseViewModel,
    navController: NavController
) {
    val totalCount by scannerViewModel.totalCount.collectAsStateWithLifecycle()
    val suspiciousCount by scannerViewModel.suspiciousCount.collectAsStateWithLifecycle()
    val currentScanId by scannerViewModel.currentScanId.collectAsStateWithLifecycle()
    val sentReportsCount by appDatabaseViewModel.sentSuspiciousCount.collectAsStateWithLifecycle(initialValue = 0)

    ScanCompleteSectionContent(
        paddingValues = paddingValues,
        totalCount = totalCount,
        suspiciousCount = suspiciousCount,
        sentReportsCount = sentReportsCount,
        onReportClick = {
            navController.navigate(Screens.RiskLevelList(scanId = currentScanId)) {
                popUpTo(Screens.Scan) {
                    inclusive = true
                }
            }
        }
    )
}

@Composable
fun ScanCompleteSectionContent(
    paddingValues: PaddingValues,
    totalCount: Int,
    suspiciousCount: Int,
    sentReportsCount: Int,
    onReportClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.BackgroundColor),
        contentPadding = paddingValues
    ) {
        item {
            ScanDoneShield()
        }

        item {
            ReportSection(
                totalApps = totalCount,
                suspiciousApps = suspiciousCount,
                sentReports = sentReportsCount,
                onButtonClick = onReportClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScanCompleteSectionPreview() {
    HashScannerTheme {
        ScanCompleteSectionContent(
            paddingValues = PaddingValues(),
            totalCount = 150,
            suspiciousCount = 12,
            sentReportsCount = 8,
            onReportClick = {}
        )
    }
}

