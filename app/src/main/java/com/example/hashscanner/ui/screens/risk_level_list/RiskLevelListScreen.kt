package com.example.hashscanner.ui.screens.risk_level_list

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hashscanner.ui.theme.HashScannerTheme
import androidx.navigation.NavController
import com.example.hashscanner.R
import com.example.hashscanner.ui.navigation.Screens
import com.example.hashscanner.ui.theme.AccentPurpleColor
import com.example.hashscanner.ui.theme.BackgroundColor
import com.example.hashscanner.ui.theme.GreenColor
import com.example.hashscanner.ui.theme.RedColor
import com.example.hashscanner.ui.theme.StrongYellowColor
import com.example.hashscanner.ui.theme.spacing
import com.example.hashscanner.ui.screens.scan_details.ScanDetailRiskCard
import com.example.hashscanner.ui.screens.scan_details.SourceOfTruthChip
import com.example.hashscanner.ui.ui_utils.AppTopBar
import com.example.hashscanner.ui.ui_utils.RiskLevelItem
import com.example.hashscanner.ui.ui_utils.RiskLevelsUI
import com.example.hashscanner.utils.Constants
import com.example.hashscanner.data.model.db_entities.AnalysisStatus
import com.example.hashscanner.data.model.db_entities.ScanHistory
import com.example.hashscanner.viewmodel.AppDatabaseViewModel

@Composable
fun RiskLevelListScreen(
    navController: NavController,
    scanId: String? = null,
    databaseViewModel: AppDatabaseViewModel
) {
    var showSystemApps by rememberSaveable { mutableStateOf(false) }
    val lastScan by databaseViewModel.lastScan.collectAsStateWithLifecycle(initialValue = null)
    val scanHistoryList by databaseViewModel.scanHistory.collectAsStateWithLifecycle(emptyList())
    val currentScanId = scanId ?: lastScan?.id

    val scanDetails = remember(scanHistoryList, currentScanId) {
        scanHistoryList.find { it.id == currentScanId }
    }

    val safeCount by databaseViewModel.safeAppsCount.collectAsStateWithLifecycle(initialValue = 0)
    val lowCount by databaseViewModel.lowRiskAppsCount.collectAsStateWithLifecycle(initialValue = 0)
    val mediumCount by databaseViewModel.mediumRiskAppsCount.collectAsStateWithLifecycle(initialValue = 0)
    val highCount by databaseViewModel.highRiskAppsCount.collectAsStateWithLifecycle(initialValue = 0)
    val criticalCount by databaseViewModel.criticalAppsCount.collectAsStateWithLifecycle(initialValue = 0)

    LaunchedEffect(currentScanId, showSystemApps) {
        if (currentScanId != null) {
            databaseViewModel.apply {
                countSafeApps(scanId = currentScanId, onlyUser = !showSystemApps)
                countLowRiskApps(scanId = currentScanId, onlyUser = !showSystemApps)
                countMediumRiskApps(scanId = currentScanId, onlyUser = !showSystemApps)
                countHighRiskApps(scanId = currentScanId, onlyUser = !showSystemApps)
                countCriticalApps(scanId = currentScanId, onlyUser = !showSystemApps)
            }
        }
    }

    val isReady = scanDetails?.analysisStatus == AnalysisStatus.COMPLETED.name

    RiskLevelListContent(
        paddingValues = PaddingValues(),
        safeCount = safeCount,
        lowCount = lowCount,
        mediumCount = mediumCount,
        highCount = highCount,
        criticalCount = criticalCount,
        isReady = isReady,
        showSystemApps = showSystemApps,
        onToggleSystemApps = { showSystemApps = !showSystemApps },
        onRobotClick = { context ->
            try {
                val intent = Intent(Intent.ACTION_VIEW, Constants.BALE_BOT_URL.toUri())
                context.startActivity(intent)
            } catch (_: Exception) {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_opening_url),
                    Toast.LENGTH_LONG
                ).show()
            }
        },
        onRiskLevelClick = { item ->
            navController.navigate(Screens.AppList(item.riskLevel, currentScanId, showSystemApps))
        },
        onBackClick = {
            navController.popBackStack()
        }
    )
}

@Composable
fun RiskLevelListContent(
    paddingValues: PaddingValues,
    safeCount: Int,
    lowCount: Int,
    mediumCount: Int,
    highCount: Int,
    criticalCount: Int,
    isReady: Boolean,
    showSystemApps: Boolean,
    onToggleSystemApps: () -> Unit,
    onRobotClick: (android.content.Context) -> Unit,
    onRiskLevelClick: (RiskLevelItem) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val riskLevels = listOf(
        RiskLevelItem(
            title = stringResource(R.string.badge_risk_level_very_high),
            count = criticalCount,
            color = MaterialTheme.colorScheme.RedColor,
            icon = Icons.Default.Warning,
            riskLevel = RiskLevelsUI.CRITICAL,
            isGoToRobot = false
        ),
        RiskLevelItem(
            title = stringResource(R.string.badge_risk_level_high),
            count = highCount,
            color = MaterialTheme.colorScheme.RedColor,
            icon = Icons.Default.Warning,
            riskLevel = RiskLevelsUI.HIGH,
            isGoToRobot = false
        ),
        RiskLevelItem(
            title = stringResource(R.string.badge_risk_level_medium),
            count = mediumCount,
            color = MaterialTheme.colorScheme.StrongYellowColor,
            icon = Icons.Default.Notifications,
            riskLevel = RiskLevelsUI.MEDIUM,
            isGoToRobot = false
        ),
        RiskLevelItem(
            title = stringResource(R.string.badge_risk_level_low),
            count = lowCount,
            color = MaterialTheme.colorScheme.GreenColor,
            icon = Icons.Default.Info,
            riskLevel = RiskLevelsUI.LOW,
            isGoToRobot = false
        ),
        RiskLevelItem(
            title = stringResource(R.string.badge_risk_level_none),
            count = safeCount,
            color = MaterialTheme.colorScheme.GreenColor,
            icon = Icons.Default.CheckCircle,
            riskLevel = RiskLevelsUI.SAFE,
            isGoToRobot = false
        )
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.BackgroundColor,
        topBar = {
            AppTopBar(
                topBarText = stringResource(R.string.topbar_title_apps),
                onClick = onBackClick,
                actions = {
                    IconButton(onClick = onToggleSystemApps) {
                        Image(
                            painter = if (showSystemApps) painterResource(R.drawable.system_apps_dark) else painterResource(R.drawable.system_apps_light),
                            contentDescription = "Toggle System Apps",
                            modifier = Modifier
                                .size(MaterialTheme.spacing.dp32)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(MaterialTheme.spacing.dp16),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.dp16),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.dp16)
        ) {
            item(span = { GridItemSpan(2) }) {
                SourceOfTruthChip(isReady = isReady)
            }

            items(riskLevels) { item ->
                ScanDetailRiskCard(
                    item = item,
                    onClick = {
                        onRiskLevelClick(item)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RiskLevelListScreenPreview() {
    HashScannerTheme {
        RiskLevelListContent(
            paddingValues = PaddingValues(),
            safeCount = 10,
            lowCount = 5,
            mediumCount = 2,
            highCount = 1,
            criticalCount = 0,
            onRobotClick = {},
            onRiskLevelClick = {},
            onBackClick = {},
            isReady = true,
            showSystemApps = false,
            onToggleSystemApps = {}
        )
    }
}