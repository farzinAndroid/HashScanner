package com.example.hashscanner.ui.screens.scan

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.hashscanner.R
import com.example.hashscanner.ui.theme.AccentPurpleColor
import com.example.hashscanner.ui.theme.WhitePurple
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.ui.theme.LightGray
import com.example.hashscanner.ui.theme.spacing

@Composable
fun CurrentlyScanSection() {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row() {




            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = Modifier
                    .size(MaterialTheme.spacing.dp64)
                    .clip(Shapes().extraLarge)
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.dp16))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = stringResource(R.string.scan_label_currently_scanning_app),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.LightGray
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp4))

                Text(
                    text = "Telegram",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.WhitePurple
                )
            }




        }



        Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp16))

        Text(
            text = "20 از 180",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.LightGray
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp12))

        CompositionLocalProvider(LocalLayoutDirection.provides(LayoutDirection.Ltr)){
            LinearProgressIndicator(
                progress = {0.5f},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.spacing.dp8)
                    .padding(horizontal = MaterialTheme.spacing.dp16),
                trackColor = MaterialTheme.colorScheme.LightGray,
                gapSize = 0.dp,
                strokeCap = StrokeCap.Square,
                drawStopIndicator = {},
                color = MaterialTheme.colorScheme.AccentPurpleColor,

                )
        }

    }


    

}

@Preview(showBackground = true)
@Composable
fun CurrentlyScanSectionPreview() {
    HashScannerTheme {
        CurrentlyScanSection()
    }
}