package com.example.hashscanner.ui.screens.app_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.hashscanner.R
import com.example.hashscanner.data.model.db_entities.AppInfo
import com.example.hashscanner.ui.theme.RedColor
import com.example.hashscanner.ui.theme.spacing
import com.example.hashscanner.ui.ui_utils.InfoCard

@Composable
fun SuspiciousReasonsCard(
    appInfo: AppInfo,
) {

    InfoCard {
        Text(
            text = stringResource(R.string.details_section_title_suspicion_reasons),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp16))

        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.dp12)) {
            val reasons =
                appInfo.riskReasons.split(",").map { it.trim() }.filter { it.isNotEmpty() }

            reasons.forEach { reason ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.dp10)
                ) {
                    Box(
                        modifier = Modifier
                            .size(MaterialTheme.spacing.dp8)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.RedColor)
                    )
                    Text(
                        text = reason,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                }
            }

        }
    }
}
