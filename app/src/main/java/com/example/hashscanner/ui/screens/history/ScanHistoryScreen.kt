package com.example.hashscanner.ui.screens.history

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hashscanner.R
import com.example.hashscanner.data.model.db_entities.ScanHistory
import com.example.hashscanner.ui.theme.AccentPurpleColor
import com.example.hashscanner.ui.theme.BackgroundColor
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.ui.theme.spacing
import com.example.hashscanner.ui.ui_utils.AppTopBar
import com.example.hashscanner.ui.ui_utils.RecentScanCard
import com.example.hashscanner.viewmodel.AppDatabaseViewModel

@Composable
fun ScanHistoryScreen(
    onBackClick: () -> Unit,
    databaseViewModel: AppDatabaseViewModel
) {
    val scanHistory by databaseViewModel.scanHistory.collectAsStateWithLifecycle(emptyList())



    ScanHistoryContent(
        scanHistory = scanHistory,
        onBackClick = onBackClick,
        onDeleteClicked = {
            databaseViewModel.deleteScanHistory(it.id)
        },
        onRecentScanCardClicked = {}
    )
}

@Composable
fun ScanHistoryContent(
    scanHistory: List<ScanHistory>,
    onBackClick: () -> Unit,
    onDeleteClicked: (ScanHistory) -> Unit,
    onRecentScanCardClicked: (ScanHistory) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredHistory = remember(scanHistory, searchQuery) {
        if (searchQuery.isEmpty()) scanHistory
        else scanHistory.filter {
            it.scanDate.contains(searchQuery) || it.scanTime.contains(
                searchQuery
            )
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                topBarText = stringResource(R.string.topbar_title_scan_history),
                onClick = onBackClick
            )
        },
        containerColor = MaterialTheme.colorScheme.BackgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = MaterialTheme.spacing.dp20)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = MaterialTheme.spacing.dp16),
                placeholder = { Text(stringResource(R.string.history_search_placeholder)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(MaterialTheme.spacing.dp12),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                    focusedBorderColor = MaterialTheme.colorScheme.AccentPurpleColor
                ),
                singleLine = true
            )

            if (filteredHistory.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.history_empty_state),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = MaterialTheme.spacing.dp24),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.dp8)
                ) {
                    items(filteredHistory) { scan ->
                        RecentScanCard(
                            scan = scan,
                            onClick = { onRecentScanCardClicked(scan) },
                            onDeleteClicked = { onDeleteClicked(scan) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScanHistoryScreenPreview() {
    HashScannerTheme {
        ScanHistoryContent(
            scanHistory = listOf(
                ScanHistory("0", "2026-08-06", "21:00", 120, 120, 110, 5, 3, 2, 0, 5000),
                ScanHistory("1", "2026-08-05", "18:30", 115, 115, 115, 0, 0, 0, 0, 4500)
            ),
            onBackClick = {},
            onDeleteClicked = {},
            onRecentScanCardClicked = {}
        )
    }
}
