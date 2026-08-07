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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hashscanner.data.model.db_entities.AppInfo
import com.example.hashscanner.ui.theme.BackgroundColor
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.ui.ui_utils.EmptyStateView
import com.example.hashscanner.ui.ui_utils.RiskLevelsUI
import com.example.hashscanner.viewmodel.AppDatabaseViewModel

@Composable
fun AppListSection(
    paddingValues: PaddingValues,
    initialRiskLevel: RiskLevelsUI,
    databaseViewModel: AppDatabaseViewModel,
    onAppClick: (String) -> Unit
) {
    var whichAppsToLoad by rememberSaveable { mutableStateOf(initialRiskLevel) }

    val currentApps by databaseViewModel.allApps.collectAsStateWithLifecycle()

    LaunchedEffect(whichAppsToLoad) {
        databaseViewModel.apply {
            when (whichAppsToLoad) {
                RiskLevelsUI.HIGH -> getHighRiskApps(onlyUser = true)
                RiskLevelsUI.MEDIUM -> getMediumRiskApps(onlyUser = true)
                RiskLevelsUI.LOW -> getLowRiskApps(onlyUser = true)
                RiskLevelsUI.SAFE -> getSafeApps(onlyUser = true)
                RiskLevelsUI.CRITICAL -> getCriticalApps(onlyUser = true)
            }
        }
    }

    AppListContent(
        paddingValues = paddingValues,
        currentApps = currentApps,
        onAppClick = onAppClick
    )
}

@Composable
fun AppListContent(
    paddingValues: PaddingValues,
    currentApps: List<AppInfo>,
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
            onAppClick = {}
        )
    }
}
