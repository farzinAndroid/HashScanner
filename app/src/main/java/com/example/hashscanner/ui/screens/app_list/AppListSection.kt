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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hashscanner.data.model.db_entities.AppInfo
import com.example.hashscanner.ui.theme.BackgroundColor
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.ui.ui_utils.RiskLevelsUI
import com.example.hashscanner.viewmodel.AppDatabaseViewmodel

@Composable
fun AppListSection(
    paddingValues: PaddingValues,
    databaseViewmodel: AppDatabaseViewmodel = hiltViewModel()
) {
    var whichAppsToLoad by remember { mutableStateOf(RiskLevelsUI.SAFE) }

    // Counts
    val highRiskCount by databaseViewmodel.highRiskAppsCount.collectAsStateWithLifecycle()
    val mediumRiskCount by databaseViewmodel.mediumRiskAppsCount.collectAsStateWithLifecycle()
    val lowRiskCount by databaseViewmodel.lowRiskAppsCount.collectAsStateWithLifecycle()
    val safeRiskCount by databaseViewmodel.safeAppsCount.collectAsStateWithLifecycle()
    val criticalRiskCount by databaseViewmodel.criticalAppsCount.collectAsStateWithLifecycle()

    // Lists
    val lowRiskApps by databaseViewmodel.lowRiskApps.collectAsStateWithLifecycle()
    val mediumRiskApps by databaseViewmodel.mediumRiskApps.collectAsStateWithLifecycle()
    val highRiskApps by databaseViewmodel.highRiskApps.collectAsStateWithLifecycle()
    val safeApps by databaseViewmodel.safeApps.collectAsStateWithLifecycle()
    val criticalApps by databaseViewmodel.criticalApps.collectAsStateWithLifecycle()

    val currentApps = when (whichAppsToLoad) {
        RiskLevelsUI.SAFE -> safeApps
        RiskLevelsUI.LOW -> lowRiskApps
        RiskLevelsUI.MEDIUM -> mediumRiskApps
        RiskLevelsUI.HIGH -> highRiskApps
        RiskLevelsUI.CRITICAL -> criticalApps
    }

    LaunchedEffect(Unit) {
        databaseViewmodel.apply {
            countHighRiskApps()
            countMediumRiskApps()
            countLowRiskApps()
            countSafeApps()
            countCriticalApps()
        }
    }

    LaunchedEffect(whichAppsToLoad) {
        databaseViewmodel.apply {
            when (whichAppsToLoad) {
                RiskLevelsUI.HIGH -> getHighRiskApps()
                RiskLevelsUI.MEDIUM -> getMediumRiskApps()
                RiskLevelsUI.LOW -> getLowRiskApps()
                RiskLevelsUI.SAFE -> getSafeApps()
                RiskLevelsUI.CRITICAL -> getCriticalApps()
            }
        }
    }

    AppListContent(
        paddingValues = paddingValues,
        whichAppsToLoad = whichAppsToLoad,
        highRiskCount = highRiskCount,
        mediumRiskCount = mediumRiskCount,
        lowRiskCount = lowRiskCount,
        safeRiskCount = safeRiskCount,
        criticalRiskCount = criticalRiskCount,
        currentApps = currentApps,
        onRiskLevelSelected = { whichAppsToLoad = it }
    )
}

@Composable
fun AppListContent(
    paddingValues: PaddingValues,
    whichAppsToLoad: RiskLevelsUI,
    highRiskCount: Int,
    mediumRiskCount: Int,
    lowRiskCount: Int,
    safeRiskCount: Int,
    criticalRiskCount: Int,
    currentApps: List<AppInfo>,
    onRiskLevelSelected: (RiskLevelsUI) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.BackgroundColor)
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            stickyHeader {
                SuspiciousBadgeSection(
                    highRiskAppsCount = highRiskCount,
                    mediumRiskAppsCount = mediumRiskCount,
                    lowRiskAppsCount = lowRiskCount,
                    safeAppsCount = safeRiskCount,
                    criticalAppsCount = criticalRiskCount,
                    onSafeAppsSelected = { onRiskLevelSelected(RiskLevelsUI.SAFE) },
                    onCriticalAppsSelected = { onRiskLevelSelected(RiskLevelsUI.CRITICAL) },
                    onLowRiskAppsSelected = { onRiskLevelSelected(RiskLevelsUI.LOW) },
                    onMediumRiskAppsSelected = { onRiskLevelSelected(RiskLevelsUI.MEDIUM) },
                    onHighRiskAppsSelected = { onRiskLevelSelected(RiskLevelsUI.HIGH) },
                )
            }

            items(currentApps) { app ->
                AppCard(app)
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
            whichAppsToLoad = RiskLevelsUI.SAFE,
            highRiskCount = 1,
            mediumRiskCount = 2,
            lowRiskCount = 3,
            safeRiskCount = 4,
            criticalRiskCount = 5,
            currentApps = emptyList(),
            onRiskLevelSelected = {}
        )
    }
}
