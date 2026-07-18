package com.example.hashscanner.ui.screens.app_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hashscanner.R
import com.example.hashscanner.ui.theme.BoxGrayColor
import com.example.hashscanner.ui.theme.RedColor
import com.example.hashscanner.ui.theme.spacing
import com.example.hashscanner.ui.ui_utils.InfoCard
import com.example.hashscanner.utils.DigitHelper

@Composable
fun RiskScoreCard(score: Int) {
    InfoCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.label_item_risk_score),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "${DigitHelper.digitByLang(score.toString())} ${stringResource(R.string.label_out_of_100)}",
                color = MaterialTheme.colorScheme.RedColor,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp16))

        LinearProgressIndicator(
            progress = { score / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.spacing.dp8)
                .clip(CircleShape),
            color = MaterialTheme.colorScheme.RedColor,
            trackColor = MaterialTheme.colorScheme.BoxGrayColor,
            strokeCap = StrokeCap.Square,
            gapSize = 0.dp
        )
    }
}
