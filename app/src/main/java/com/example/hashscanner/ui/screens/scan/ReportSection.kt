package com.example.hashscanner.ui.screens.scan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hashscanner.R
import com.example.hashscanner.ui.theme.AccentPurpleColor
import com.example.hashscanner.ui.theme.GreenColor
import com.example.hashscanner.ui.theme.RedColor
import com.example.hashscanner.ui.theme.spacing
import com.example.hashscanner.ui.ui_utils.InfoCard
import com.example.hashscanner.ui.ui_utils.MainPurpleButton
import com.example.hashscanner.utils.DigitHelper

@Composable
fun ReportSection(
    modifier: Modifier = Modifier,
    totalApps: Int,
    suspiciousApps: Int,
    sentReports: Int = 0,
    onButtonClick:()-> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp32))

        ReportCard(
            icon = painterResource(R.drawable.app),
            title = stringResource(R.string.scan_statistic_total_apps_count),
            subtitle = DigitHelper.digitByLang(totalApps.toString()),
            color = MaterialTheme.colorScheme.AccentPurpleColor
        )




        Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp24))

        ReportCard(
            icon = painterResource(R.drawable.mark),
            title = stringResource(R.string.topbar_title_suspicious_apps),
            subtitle = DigitHelper.digitByLang(suspiciousApps.toString()),
            color = MaterialTheme.colorScheme.RedColor
        )


        Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp24))

        ReportCard(
            icon = painterResource(R.drawable.cloud),
            title = stringResource(R.string.result_item_reports_sent_to_server),
            subtitle = DigitHelper.digitByLang(sentReports.toString()),
            color = MaterialTheme.colorScheme.GreenColor
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp24))

        MainPurpleButton(
            text = stringResource(R.string.button_view_detailed_results),
            onClick = onButtonClick,
            modifier = Modifier
                .padding(horizontal = MaterialTheme.spacing.dp32)
        )


    }


}