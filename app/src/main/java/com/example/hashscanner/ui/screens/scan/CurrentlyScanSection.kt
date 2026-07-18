package com.example.hashscanner.ui.screens.scan

import android.graphics.Bitmap
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.hashscanner.R
import com.example.hashscanner.ui.theme.AccentPurpleColor
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.ui.theme.LightGray
import com.example.hashscanner.ui.theme.WhitePurple
import com.example.hashscanner.ui.theme.spacing
import com.example.hashscanner.utils.DigitHelper

@Composable
fun CurrentlyScanSection(
    icon: Bitmap?,
    appName: String,
    progress: Float,
    scannedCount: Int,
    totalCount: Int,
) {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row() {


            if (icon == null) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier
                        .size(MaterialTheme.spacing.dp64)
                        .clip(Shapes().extraLarge),
                    contentScale = ContentScale.Fit
                )
            } else {
                Image(
                    bitmap = icon.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(MaterialTheme.spacing.dp64)
                        .clip(Shapes().extraLarge),
                    contentScale = ContentScale.Fit
                )
            }


            Spacer(modifier = Modifier.width(MaterialTheme.spacing.dp16))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.scan_label_currently_scanning_app),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.LightGray
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp4))

                Text(
                    text = appName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.WhitePurple,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .width(250.dp),
                    textAlign = TextAlign.Center
                )
            }


        }



        Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp16))

        Text(
            text = "${DigitHelper.digitByLang(scannedCount.toString())} ${stringResource(R.string.label_of)} ${
                DigitHelper.digitByLang(
                    totalCount.toString()
                )
            }",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.LightGray
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp12))

        CompositionLocalProvider(LocalLayoutDirection.provides(LayoutDirection.Ltr)) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.spacing.dp8)
                    .padding(horizontal = MaterialTheme.spacing.dp16),
                trackColor = MaterialTheme.colorScheme.LightGray,
                gapSize = 0.dp,
                strokeCap = StrokeCap.Butt,
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
        CurrentlyScanSection(null, "Telegram", 0.5f, 100, 100)
    }
}
