package com.example.hashscanner.ui.screens.app_list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.hashscanner.ui.theme.BlackWhiteColor

@Composable
fun RiskBadge(
    riskText: String,
    color: Color
) {





    Box(
        modifier = Modifier
            .width(100.dp)
            .height(30.dp)
            .clip(Shapes().medium)
            .border(
                width = 1.dp,
                color = color,
                shape = Shapes().medium
            )
            .background(color.copy(0.3f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = riskText,
            color = MaterialTheme.colorScheme.BlackWhiteColor,
            style = MaterialTheme.typography.bodySmall
        )
    }

}