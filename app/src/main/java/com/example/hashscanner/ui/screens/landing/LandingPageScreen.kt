package com.example.hashscanner.ui.screens.landing

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hashscanner.ui.theme.Typography
import com.example.hashscanner.ui.theme.Vazirmatn

@Composable
fun LandingPageScreen(
    modifier: Modifier = Modifier
) {

    Text(
        "سلام",
        style = MaterialTheme.typography.headlineMedium,
    )

}