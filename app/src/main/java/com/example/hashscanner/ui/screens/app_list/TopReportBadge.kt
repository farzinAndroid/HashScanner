package com.example.hashscanner.ui.screens.app_list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.hashscanner.R
import com.example.hashscanner.ui.theme.BlackWhiteColor
import com.example.hashscanner.ui.theme.spacing
import com.example.hashscanner.utils.DigitHelper

@Composable
fun TopReportBadge(
    count: Int,
    modifier: Modifier = Modifier,
    color: Color,
    textColor: Color,
    text: String
) {

    Card(
        modifier = modifier
            .height(100.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(0.3f)
        ),
        shape = Shapes().medium,
        border = BorderStroke(
            width = 1.dp,
            color = color,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(R.drawable.shield_done),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = MaterialTheme.spacing.dp4)
                    .height(30.dp),
                colorFilter = ColorFilter.tint(textColor)
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp6))

            Text(
                text = DigitHelper.digitByLang(count.toString()),
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp6))


            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.BlackWhiteColor
            )
        }
    }

}