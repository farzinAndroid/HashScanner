package com.example.hashscanner.ui.screens.history_details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.hashscanner.R
import com.example.hashscanner.data.model.api.ScanResultResponse
import com.example.hashscanner.data.model.db_entities.ScanHistory
import com.example.hashscanner.data.network.NetworkResult
import com.example.hashscanner.ui.navigation.Screens
import com.example.hashscanner.ui.theme.*
import com.example.hashscanner.ui.ui_utils.AppTopBar
import com.example.hashscanner.ui.ui_utils.RiskLevelItem
import com.example.hashscanner.ui.ui_utils.RiskLevelsUI
import com.example.hashscanner.viewmodel.AppDatabaseViewModel

@Composable
fun HistoryDetailsScreen(
    navController: NavController,
    scanId: String?,
    databaseViewModel: AppDatabaseViewModel,
    scannerViewModel: com.example.hashscanner.viewmodel.ScannerViewModel
) {
    val scanHistoryList by databaseViewModel.scanHistory.collectAsStateWithLifecycle(emptyList())
    val scanResult by scannerViewModel.scanResultResponse.collectAsStateWithLifecycle()

    val scanDetails = remember(scanHistoryList, scanId) {
        scanHistoryList.find { it.id == scanId }
    }

    LaunchedEffect(scanId) {
        if (scanId != null) {
            scannerViewModel.getScanResult(scanId)
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
            HistoryDetailsContent(
                paddingValues = paddingValues,
                scan = scanDetails,
                scanResult = scanResult,
                onRiskLevelClick = { riskLevel ->
                    navController.navigate(Screens.AppList(riskLevel, scanId))
                }
            )
        }
    }
}

@Composable
fun HistoryDetailsContent(
    paddingValues: PaddingValues,
    scan: ScanHistory,
    scanResult: NetworkResult<ScanResultResponse>,
    onRiskLevelClick: (RiskLevelsUI) -> Unit
) {
    val totalSuspicious = scan.highRisk + scan.criticalRisk
    val resultColor = if (totalSuspicious > 0) MaterialTheme.colorScheme.RedColor else MaterialTheme.colorScheme.GreenColor

    val riskItems = listOf(
        RiskLevelItem(
            title = stringResource(R.string.badge_risk_level_very_high),
            count = scan.criticalRiskUserApps,
            color = MaterialTheme.colorScheme.RedColor,
            icon = Icons.Default.Warning,
            riskLevel = RiskLevelsUI.CRITICAL,
            isGoToRobot = false
        ),
        RiskLevelItem(
            title = stringResource(R.string.badge_risk_level_high),
            count = scan.highRiskUserApps,
            color = MaterialTheme.colorScheme.RedColor,
            icon = Icons.Default.Warning,
            riskLevel = RiskLevelsUI.HIGH,
            isGoToRobot = false
        ),
        RiskLevelItem(
            title = stringResource(R.string.badge_risk_level_medium),
            count = scan.mediumRiskUserApps,
            color = MaterialTheme.colorScheme.StrongYellowColor,
            icon = Icons.Default.Notifications,
            riskLevel = RiskLevelsUI.MEDIUM,
            isGoToRobot = false
        ),
        RiskLevelItem(
            title = stringResource(R.string.badge_risk_level_low),
            count = scan.lowRiskUserApps,
            color = MaterialTheme.colorScheme.GreenColor,
            icon = Icons.Default.Info,
            riskLevel = RiskLevelsUI.LOW,
            isGoToRobot = false
        ),
        RiskLevelItem(
            title = stringResource(R.string.badge_risk_level_none),
            count = scan.safeUserApps,
            color = MaterialTheme.colorScheme.GreenColor,
            icon = Icons.Default.CheckCircle,
            riskLevel = RiskLevelsUI.SAFE,
            isGoToRobot = false
        )
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // --- Header Section ---
        item(span = { GridItemSpan(2) }) {
            HistoryHeader(scan, resultColor, totalSuspicious)
        }

        // --- API Result Section ---
        if (scanResult is NetworkResult.Success) {
            val data = scanResult.data
            if (data != null) {
                if (data.ready) {
                    val initialReport = data.initialReport
                    val finalReport = data.finalReport
                    val uploadedApks = data.uploadedApks

                    // PRIORITY 1: Show Final Summary if Complete
                    if (finalReport?.complete == true) {
                        item(span = { GridItemSpan(2) }) {
                            ApiSummaryCard(
                                result = if (finalReport.summary.virus > 0) "VIRUS" else "CLEAN",
                                message = finalReport.message ?: stringResource(R.string.notification_analysis_complete_generic)
                            )
                        }
                    } 
                    // PRIORITY 2: Show Initial Summary if Final is not ready
                    else if (initialReport != null) {
                        item(span = { GridItemSpan(2) }) {
                            ApiSummaryCard(
                                result = if (initialReport.summary.virus > 0) "VIRUS" else "CLEAN",
                                message = stringResource(R.string.report_initial_summary, initialReport.summary.virus, initialReport.summary.suspicious)
                            )
                        }
                    }

                    // --- Uploaded APKs Progress Section ---
                    uploadedApks?.let { uploaded ->
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

                    // Always show the app list if we have initial apps (server updates these rows)
                    initialReport?.let { initial ->
                        if (initial.apps.isNotEmpty()) {
                            item(span = { GridItemSpan(2) }) {
                                Text(
                                    text = stringResource(R.string.api_analysis_results_title),
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.BlackWhiteColor,
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                            items(initial.apps, span = { GridItemSpan(2) }) { app ->
                                ApiAppResultCard(app)
                            }
                        }
                    }

                    // Loophole Fix: Check both finalReport AND uploadedApks
                    val isWaitingForApks = uploadedApks?.summary?.complete == false && (uploadedApks.summary.total > 0)
                    val isWaitingForFinal = finalReport == null || !finalReport.complete
                    
                    if (isWaitingForApks || isWaitingForFinal) {
                        item(span = { GridItemSpan(2) }) {
                            Text(
                                text = stringResource(R.string.report_finalizing),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                } else {
                    // Show Progress if not ready
                    item(span = { GridItemSpan(2) }) {
                        ApiProgressCard(
                            message = data.message ?: "",
                            total = data.totalReports ?: 0,
                            checked = data.checkedReports ?: 0,
                            pending = data.pendingReports ?: 0
                        )
                    }
                }
            }
        }

        // --- Risk Grid ---
        items(riskItems) { item ->
            HistoryRiskCard(item) {
                onRiskLevelClick(item.riskLevel)
            }
        }

        // --- Metadata Section ---
        item(span = { GridItemSpan(2) }) {
            HistoryMetadataSection(scan)
        }
    }
}
