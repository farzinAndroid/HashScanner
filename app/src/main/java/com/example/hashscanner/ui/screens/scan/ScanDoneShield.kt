package com.example.hashscanner.ui.screens.scan

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hashscanner.R
import com.example.hashscanner.ui.theme.GreenColor
import com.example.hashscanner.ui.theme.LightGray
import com.example.hashscanner.ui.theme.spacing

@Composable
fun ScanDoneShield(
    modifier: Modifier = Modifier
) {


    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Image(
            painter = painterResource(R.drawable.shield_done),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.GreenColor),
            modifier = Modifier
                .size(100.dp)
        )


        Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp32))

        Text(
            text = stringResource(R.string.result_header_scan_completed_successfully),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.GreenColor,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )


        Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp8))

        Text(
            text = stringResource(R.string.result_description_all_apps_checked),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.LightGray,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold
        )



    }


}