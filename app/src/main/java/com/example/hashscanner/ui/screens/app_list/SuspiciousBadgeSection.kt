package com.example.hashscanner.ui.screens.app_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.hashscanner.R
import com.example.hashscanner.ui.theme.GreenColor
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.ui.theme.RedColor
import com.example.hashscanner.ui.theme.StrongYellowColor
import com.example.hashscanner.ui.theme.YellowColor
import com.example.hashscanner.ui.theme.spacing

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


    LazyRow(
        modifier = Modifier
            .padding(top = MaterialTheme.spacing.dp16)
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.Center
    ) {


        item {
            TopReportBadge(
                count = safeAppsCount,
                color = MaterialTheme.colorScheme.GreenColor,
                textColor = MaterialTheme.colorScheme.GreenColor,
                text = stringResource(R.string.badge_risk_level_none),
                onClick = onSafeAppsSelected
            )
        }

        item { Spacer(modifier = Modifier.width(MaterialTheme.spacing.dp16)) }

        item {
            TopReportBadge(
                count = lowRiskAppsCount,
                color = MaterialTheme.colorScheme.GreenColor,
                textColor = MaterialTheme.colorScheme.GreenColor,
                text = stringResource(R.string.badge_risk_level_low),
                onClick = onLowRiskAppsSelected
            )
        }

        item { Spacer(modifier = Modifier.width(MaterialTheme.spacing.dp16)) }


        item {
            TopReportBadge(
                count = mediumRiskAppsCount,
                color = MaterialTheme.colorScheme.YellowColor,
                textColor = MaterialTheme.colorScheme.StrongYellowColor,
                text = stringResource(R.string.badge_risk_level_medium),
                onClick = onMediumRiskAppsSelected
            )
        }



        item { Spacer(modifier = Modifier.width(MaterialTheme.spacing.dp16)) }

        item {
            TopReportBadge(
                count = criticalAppsCount,
                color = MaterialTheme.colorScheme.RedColor,
                textColor = MaterialTheme.colorScheme.RedColor,
                text = stringResource(R.string.badge_risk_level_very_high),
                onClick = onCriticalAppsSelected
            )
        }



        item {
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.dp16))
        }

        item {
            TopReportBadge(
                count = highRiskAppsCount,
                color = MaterialTheme.colorScheme.RedColor,
                textColor = MaterialTheme.colorScheme.RedColor,
                text = stringResource(R.string.badge_risk_level_high),
                onClick = onHighRiskAppsSelected
            )
        }

    }


}

@Preview(showBackground = true)
@Composable
fun SuspiciousBadgeSectionPreview() {
    HashScannerTheme {
        SuspiciousBadgeSection(
            1,
            1,
            1,
            1,
            1,
            onSafeAppsSelected = {},
            onLowRiskAppsSelected = {},
            onMediumRiskAppsSelected = {},
            onHighRiskAppsSelected = {},
            onCriticalAppsSelected = {}
        )
    }
}