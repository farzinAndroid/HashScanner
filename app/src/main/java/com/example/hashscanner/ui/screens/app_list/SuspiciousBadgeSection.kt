package com.example.hashscanner.ui.screens.app_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.hashscanner.R
import com.example.hashscanner.ui.theme.BackgroundColor
import com.example.hashscanner.ui.theme.GreenColor
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.ui.theme.RedColor
import com.example.hashscanner.ui.theme.StrongYellowColor
import com.example.hashscanner.ui.theme.YellowColor
import com.example.hashscanner.ui.theme.spacing
import com.example.hashscanner.ui.ui_utils.BadgeData


@Composable
fun SuspiciousBadgeSection(
    highRiskAppsCount: Int,
    mediumRiskAppsCount: Int,
    lowRiskAppsCount: Int,
    safeAppsCount: Int,
    criticalAppsCount: Int,
    onSafeAppsSelected: () -> Unit,
    onLowRiskAppsSelected: () -> Unit,
    onMediumRiskAppsSelected: () -> Unit,
    onHighRiskAppsSelected: () -> Unit,
    onCriticalAppsSelected: () -> Unit
) {
    val badges = listOf(
        BadgeData(
            count = safeAppsCount,
            color = MaterialTheme.colorScheme.GreenColor,
            textColor = MaterialTheme.colorScheme.GreenColor,
            text = stringResource(R.string.badge_risk_level_none),
            onClick = onSafeAppsSelected
        ),
        BadgeData(
            count = lowRiskAppsCount,
            color = MaterialTheme.colorScheme.GreenColor,
            textColor = MaterialTheme.colorScheme.GreenColor,
            text = stringResource(R.string.badge_risk_level_low),
            onClick = onLowRiskAppsSelected
        ),
        BadgeData(
            count = mediumRiskAppsCount,
            color = MaterialTheme.colorScheme.YellowColor,
            textColor = MaterialTheme.colorScheme.StrongYellowColor,
            text = stringResource(R.string.badge_risk_level_medium),
            onClick = onMediumRiskAppsSelected
        ),
        BadgeData(
            count = criticalAppsCount,
            color = MaterialTheme.colorScheme.RedColor,
            textColor = MaterialTheme.colorScheme.RedColor,
            text = stringResource(R.string.badge_risk_level_very_high),
            onClick = onCriticalAppsSelected
        ),
        BadgeData(
            count = highRiskAppsCount,
            color = MaterialTheme.colorScheme.RedColor,
            textColor = MaterialTheme.colorScheme.RedColor,
            text = stringResource(R.string.badge_risk_level_high),
            onClick = onHighRiskAppsSelected
        )
    )

    LazyRow(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.BackgroundColor)
            .fillMaxWidth()
            .wrapContentHeight(),
        contentPadding = PaddingValues(vertical = MaterialTheme.spacing.dp16),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            space = MaterialTheme.spacing.dp16,
            alignment = Alignment.CenterHorizontally
        )
    ) {
        items(badges) { badge ->
            TopReportBadge(
                count = badge.count,
                color = badge.color,
                textColor = badge.textColor,
                text = badge.text,
                onClick = badge.onClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SuspiciousBadgeSectionPreview() {
    HashScannerTheme {
        SuspiciousBadgeSection(
            highRiskAppsCount = 1,
            mediumRiskAppsCount = 1,
            lowRiskAppsCount = 1,
            safeAppsCount = 1,
            criticalAppsCount = 1,
            onSafeAppsSelected = {},
            onLowRiskAppsSelected = {},
            onMediumRiskAppsSelected = {},
            onHighRiskAppsSelected = {},
            onCriticalAppsSelected = {}
        )
    }
}
