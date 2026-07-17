package com.example.hashscanner.ui.ui_utils

import androidx.compose.ui.graphics.Color

data class BadgeData(
    val count: Int,
    val color: Color,
    val textColor: Color,
    val text: String,
    val riskLevel: RiskLevelsUI,
    val onClick: () -> Unit
)
