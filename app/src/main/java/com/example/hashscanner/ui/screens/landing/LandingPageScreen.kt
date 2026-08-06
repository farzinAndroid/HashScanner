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
import com.example.hashscanner.R
import com.example.hashscanner.data.model.db_entities.ScanHistory
import com.example.hashscanner.ui.theme.AccentPurpleColor
import com.example.hashscanner.ui.theme.BackgroundColor
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.ui.theme.spacing
import com.example.hashscanner.ui.ui_utils.RecentScanCard
import com.example.hashscanner.viewmodel.AppDatabaseViewModel
import com.example.hashscanner.viewmodel.AppViewModel

@Composable
fun LandingPageScreen(
    onScanClick: () -> Unit,
    onHistoryClick: () -> Unit,
    appViewModel: AppViewModel,
    databaseViewModel: AppDatabaseViewModel
) {
    val scanHistory by databaseViewModel.scanHistory.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        databaseViewModel.getAllScanHistory()
        databaseViewModel.getLastScan()
    }

    LandingPageContent(
        scanHistory = scanHistory,
        onScanClick = onScanClick,
        onHistoryClick = onHistoryClick
    )
}

@Composable
fun LandingPageContent(
    scanHistory: List<ScanHistory>,
    onScanClick: () -> Unit,
    onHistoryClick: () -> Unit
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
                    onClick = { /* Could navigate to scan details if implemented */ }
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
                ScanHistory(0, "2026-08-06", "21:00", 120, 120, 110, 5, 3, 2, 0, 5000),
                ScanHistory(1, "2026-08-05", "18:30", 115, 115, 115, 0, 0, 0, 0, 4500)
            ),
            onScanClick = {},
            onHistoryClick = {}
        )
    }
}
