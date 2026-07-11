package com.example.hashscanner.ui.screens.scan

import android.annotation.SuppressLint
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hashscanner.ui.theme.BackgroundColor
import com.example.hashscanner.ui.ui_utils.AppTopBar


@Composable
fun ScanScreen(
    modifier: Modifier = Modifier
) {
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