package com.example.hashscanner.ui.screens.history_details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.hashscanner.R
import com.example.hashscanner.data.model.db_entities.ScanHistory
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
    databaseViewModel: AppDatabaseViewModel
) {
    val scanHistoryList by databaseViewModel.scanHistory.collectAsStateWithLifecycle(emptyList())
    val scanDetails = remember(scanHistoryList, scanId) {
        scanHistoryList.find { it.id == scanId }
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

@Preview(showBackground = true)
@Composable
fun HistoryDetailsPreview() {
    HashScannerTheme {
        HistoryDetailsContent(
            paddingValues = PaddingValues(0.dp),
            scan = ScanHistory(
                id = "1",
                scanDate = "2026-08-10",
                scanTime = "20:00",
                totalApps = 150,
                scannedApps = 150,
                safeApps = 140,
                lowRisk = 5,
                mediumRisk = 3,
                highRisk = 2,
                criticalRisk = 0,
                duration = 5400L,
                safeUserApps = 140,
                lowRiskUserApps = 5,
                mediumRiskUserApps = 3,
                highRiskUserApps = 2,
                criticalRiskUserApps = 0,
                systemApps = 10,
                userApps = 50
            ),
            onRiskLevelClick = {}
        )
    }
}
