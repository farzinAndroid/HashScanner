package com.example.hashscanner.ui.screens.landing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.hashscanner.R
import com.example.hashscanner.data.model.db_entities.ScanHistory
import com.example.hashscanner.ui.navigation.Screens
import com.example.hashscanner.ui.theme.AccentPurpleColor
import com.example.hashscanner.ui.theme.BackgroundColor
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.ui.theme.LightGray
import com.example.hashscanner.ui.theme.spacing
import com.example.hashscanner.ui.ui_utils.RecentScanCard
import com.example.hashscanner.viewmodel.AppDatabaseViewModel
import com.example.hashscanner.viewmodel.AppViewModel
import kotlin.collections.emptyList

@Composable
fun LandingPageScreen(
    onScanClick: () -> Unit,
    onHistoryClick: () -> Unit,
    appViewModel: AppViewModel,
    databaseViewModel: AppDatabaseViewModel,
    navController: NavController
) {
    val scanHistory by databaseViewModel.scanHistory.collectAsStateWithLifecycle(emptyList())
    val lastScan by databaseViewModel.lastScan.collectAsStateWithLifecycle(initialValue = null)

    LandingPageContent(
        scanHistory = scanHistory,
        onScanClick = onScanClick,
        onHistoryClick = onHistoryClick,
        onDeleteClicked = {
            databaseViewModel.deleteScanHistory(it.id)
        },
        onRecentScanCardClicked = {
            navController.navigate(Screens.HistoryDetails(it.id))
        }
    )
}

@Composable
fun LandingPageContent(
    scanHistory: List<ScanHistory>,
    onScanClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onDeleteClicked: (ScanHistory) -> Unit,
    onRecentScanCardClicked: (ScanHistory) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.BackgroundColor)
            .padding(horizontal = MaterialTheme.spacing.dp20),
        contentPadding = PaddingValues(bottom = MaterialTheme.spacing.dp24)
    ) {
        item {
            DashboardHeader()
        }

        // Quick Actions
        item {
            SectionHeader(title = stringResource(R.string.dashboard_quick_actions))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.dp16)
            ) {
                QuickActionCard(
                    title = stringResource(R.string.button_start_scan),
                    icon = Icons.Default.Info,
                    containerColor = MaterialTheme.colorScheme.AccentPurpleColor,
                    contentColor = Color.White,
                    modifier = Modifier.weight(1f),
                    onClick = onScanClick
                )
                QuickActionCard(
                    title = stringResource(R.string.dashboard_action_history),
                    icon = Icons.AutoMirrored.Filled.List,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.AccentPurpleColor,
                    modifier = Modifier.weight(1f),
                    onClick = onHistoryClick
                )
            }
        }

        // Recent Scans
        item {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp16))
            SectionHeader(
                title = stringResource(R.string.dashboard_recent_scans),
                onViewAllClick = if (scanHistory.isNotEmpty()) onHistoryClick else null
            )
        }

        if (scanHistory.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = MaterialTheme.spacing.dp32),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.dashboard_no_history),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                }
            }
        } else {
            items(scanHistory.take(3)) { scan ->
                RecentScanCard(
                    scan = scan,
                    modifier = Modifier.padding(vertical = MaterialTheme.spacing.dp6),
                    onClick = { onRecentScanCardClicked(scan) },
                    onDeleteClicked = { onDeleteClicked(scan) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LandingPageScreenPreview() {
    HashScannerTheme {
        LandingPageContent(
            scanHistory = listOf(
                ScanHistory(
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
                ScanHistory(
                    id = "2",
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
                )
            ),
            onScanClick = {},
            onHistoryClick = {},
            onDeleteClicked = {},
            onRecentScanCardClicked = {}
        )
    }
}
