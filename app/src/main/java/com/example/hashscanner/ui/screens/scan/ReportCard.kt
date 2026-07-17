package com.example.hashscanner.ui.screens.scan

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hashscanner.ui.theme.spacing
import com.example.hashscanner.ui.ui_utils.InfoCard

@Composable
fun ReportCard(
    icon: Painter,
    title: String,
    subtitle: String,
    color: Color
) {

    InfoCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(74.dp)
            .padding(horizontal = MaterialTheme.spacing.dp32),
        contentPadding = PaddingValues(0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = subtitle,
                color = color,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(start = MaterialTheme.spacing.dp12),
                fontWeight = FontWeight.Bold
            )


            Text(
                text = title,
                color = color,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            Image(
                painter = icon,
                contentDescription = null,
                colorFilter = ColorFilter.tint(color),
                modifier = Modifier
                    .padding(end = MaterialTheme.spacing.dp12)
                    .size(MaterialTheme.spacing.dp24),

            )

        }
    }

}