package com.example.hashscanner.ui.ui_utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class RiskLevelItem(
    val title: String,
    val count: Int,
    val color: Color,
    val icon: ImageVector,
    val riskLevel: RiskLevelsUI,
    val isGoToRobot: Boolean
)