package com.example.hashscanner.ui.screens.scan

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hashscanner.ui.theme.BackgroundColor
import com.example.hashscanner.ui.ui_utils.AppTopBar


@Composable
fun ScanScreen() {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.BackgroundColor,
        topBar = { AppTopBar() },
        content = { paddingValues ->
            ScanningProgressSection(
                paddingValues = paddingValues
            )
        }
    )
}