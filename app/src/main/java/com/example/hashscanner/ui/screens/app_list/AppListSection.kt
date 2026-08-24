package com.example.hashscanner.ui.screens.app_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hashscanner.data.model.db_entities.AnalysisStatus
import com.example.hashscanner.data.model.db_entities.AppInfo
import com.example.hashscanner.ui.screens.scan_details.SourceOfTruthChip
import com.example.hashscanner.ui.theme.BackgroundColor
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.ui.ui_utils.EmptyStateView
import com.example.hashscanner.ui.ui_utils.RiskLevelsUI
import com.example.hashscanner.viewmodel.AppDatabaseViewModel

@Composable
fun AppListSection(
    paddingValues: PaddingValues,
    initialRiskLevel: RiskLevelsUI,
    scanId: String? = null,
    databaseViewModel: AppDatabaseViewModel,
    showSystem: Boolean,
    onAppClick: (String) -> Unit
) {
    val lastScan by databaseViewModel.lastScan.collectAsStateWithLifecycle(initialValue = null)
    val scanHistoryList by databaseViewModel.scanHistory.collectAsStateWithLifecycle(emptyList())
    val currentScanId = scanId ?: lastScan?.id

    val scanDetails = remember(scanHistoryList, currentScanId) {
        scanHistoryList.find { it.id == currentScanId }
    }

    var whichAppsToLoad by rememberSaveable { mutableStateOf(initialRiskLevel) }

    val currentApps by databaseViewModel.allApps.collectAsStateWithLifecycle()

    LaunchedEffect(whichAppsToLoad, currentScanId, showSystem) {
        if (currentScanId != null) {
            databaseViewModel.apply {
                when (whichAppsToLoad) {
                    RiskLevelsUI.HIGH -> getHighRiskApps(currentScanId, onlyUser = !showSystem)
                    RiskLevelsUI.MEDIUM -> getMediumRiskApps(currentScanId, onlyUser = !showSystem)
                    RiskLevelsUI.LOW -> getLowRiskApps(currentScanId, onlyUser = !showSystem)
                    RiskLevelsUI.SAFE -> getSafeApps(currentScanId, onlyUser = !showSystem)
                    RiskLevelsUI.CRITICAL -> getCriticalApps(currentScanId, onlyUser = !showSystem)
                }
            }
        }
    }

    val isReady = scanDetails?.analysisStatus == AnalysisStatus.COMPLETED.name

    AppListContent(
        paddingValues = paddingValues,
        currentApps = currentApps,
        onAppClick = onAppClick,
        isReady = isReady
    )
}

@Composable
fun AppListContent(
    paddingValues: PaddingValues,
    currentApps: List<AppInfo>,
    isReady: Boolean,
    onAppClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.BackgroundColor)
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (currentApps.isEmpty()) {
            EmptyStateView()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                item {
                    SourceOfTruthChip(isReady = isReady)
                }

                items(currentApps) { app ->
                    AppCard(
                        appInfo = app,
                        onClick = { onAppClick(app.packageName) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppListSectionPreview() {
    HashScannerTheme {
        AppListContent(
            paddingValues = PaddingValues(),
            currentApps = emptyList(),
            isReady = true,
            onAppClick = {}
        )
    }
}
