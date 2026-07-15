package com.example.hashscanner.ui.screens.app_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
    lowRiskAppsCount: Int
) {


    Row(
        modifier = Modifier
            .padding(top = MaterialTheme.spacing.dp16)
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.Center
    ) {

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.dp16))
        TopReportBadge(
            count = highRiskAppsCount,
            modifier = Modifier
                .weight(0.3f),
            color = MaterialTheme.colorScheme.RedColor,
            textColor = MaterialTheme.colorScheme.RedColor,
            text = stringResource(R.string.badge_risk_level_high)
        )


        Spacer(modifier = Modifier.width(MaterialTheme.spacing.dp16))
        TopReportBadge(
            count = mediumRiskAppsCount,
            modifier = Modifier
                .weight(0.3f),
            color = MaterialTheme.colorScheme.YellowColor,
            textColor = MaterialTheme.colorScheme.StrongYellowColor,
            text = stringResource(R.string.badge_risk_level_medium)
        )
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.dp16))
        TopReportBadge(
            count = lowRiskAppsCount,
            modifier = Modifier
                .weight(0.3f),
            color = MaterialTheme.colorScheme.GreenColor,
            textColor = MaterialTheme.colorScheme.GreenColor,
            text = stringResource(R.string.badge_risk_level_low)
        )
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.dp16))
    }


}

@Preview(showBackground = true)
@Composable
fun SuspiciousBadgeSectionPreview() {
    HashScannerTheme {
        SuspiciousBadgeSection(1,1,1,)
    }
}