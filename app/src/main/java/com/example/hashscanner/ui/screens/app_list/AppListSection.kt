package com.example.hashscanner.ui.screens.app_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hashscanner.R
import com.example.hashscanner.data.model.db_entities.AnalysisStatus
import com.example.hashscanner.data.model.db_entities.AppInfo
import com.example.hashscanner.ui.screens.scan_details.SourceOfTruthChip
import com.example.hashscanner.ui.theme.AccentPurpleColor
import com.example.hashscanner.ui.theme.BackgroundColor
import com.example.hashscanner.ui.theme.BoxGrayColor
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.ui.theme.spacing
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
    var searchQuery by rememberSaveable { mutableStateOf("") }

    val currentApps by databaseViewModel.allApps.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        onDispose {
            databaseViewModel.clearApps()
        }
    }

    // Room DB search effect with 200ms debounce
    LaunchedEffect(searchQuery, currentScanId, showSystem, whichAppsToLoad) {
        if (searchQuery.isNotBlank() && currentScanId != null) {
            delay(200)
            databaseViewModel.searchApps(
                keyword = searchQuery, 
                scanId = currentScanId, 
                riskLevel = whichAppsToLoad.name,
                onlyUser = !showSystem
            )
        }
    }

    // Category apps loading effect (when search query is blank)
    LaunchedEffect(whichAppsToLoad, searchQuery, currentScanId, showSystem) {
        if (searchQuery.isBlank() && currentScanId != null) {
            databaseViewModel.clearApps()
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
        searchQuery = searchQuery,
        onSearchQueryChange = { searchQuery = it },
        onAppClick = onAppClick,
        isReady = isReady
    )
}

@Composable
fun AppListContent(
    paddingValues: PaddingValues,
    currentApps: List<AppInfo>,
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
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
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.dp16, vertical = MaterialTheme.spacing.dp8),
            placeholder = {
                Text(
                    text = stringResource(R.string.placeholder_search_apps),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.AccentPurpleColor
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(MaterialTheme.spacing.dp16),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.AccentPurpleColor,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
                focusedContainerColor = MaterialTheme.colorScheme.BoxGrayColor,
                unfocusedContainerColor = MaterialTheme.colorScheme.BoxGrayColor
            )
        )

        if (currentApps.isEmpty()) {
            EmptyStateView()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                item {
                    SourceOfTruthChip(isReady = isReady)
                }

                items(
                    items = currentApps,
                    key = { it.packageName }
                ) { app ->
                        AppCard(
                            appInfo = app,
                            onClick = { onAppClick(app.packageName) },
                            modifier = Modifier.animateItem()
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
