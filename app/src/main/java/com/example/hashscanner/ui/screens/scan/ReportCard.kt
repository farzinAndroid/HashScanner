package com.example.hashscanner.ui.screens.scan

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.hashscanner.ui.theme.BlackWhiteColor
import com.example.hashscanner.ui.theme.spacing

@Composable
fun ReportCard(
    icon: Painter,
    title: String,
    subtitle: String,
    color: Color
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(MaterialTheme.spacing.dp64)
            .padding(horizontal = MaterialTheme.spacing.dp32),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.BlackWhiteColor),
        shape = Shapes().medium,
        colors = CardDefaults.cardColors(
            containerColor = color.copy(0.1f)
        ),
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
                    .padding(start = MaterialTheme.spacing.dp8)
            )


            Text(
                text = title,
                color = color,
                style = MaterialTheme.typography.bodyLarge
            )

            Image(
                painter = icon,
                contentDescription = null,
                colorFilter = ColorFilter.tint(color),
                modifier = Modifier
                    .size(MaterialTheme.spacing.dp32)
                    .padding(end = MaterialTheme.spacing.dp8)
            )

        }
    }

}