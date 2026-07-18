package com.example.hashscanner.ui.screens.app_list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hashscanner.R
import com.example.hashscanner.ui.theme.BlackWhiteColor
import com.example.hashscanner.ui.theme.spacing
import com.example.hashscanner.ui.ui_utils.InfoCard
import com.example.hashscanner.utils.DigitHelper

@Composable
fun TopReportBadge(
    count: Int,
    modifier: Modifier = Modifier,
    color: Color,
    textColor: Color,
    text: String,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {

    Card(
        modifier = modifier
            .padding(start = MaterialTheme.spacing.dp8)
            .padding(end = MaterialTheme.spacing.dp8)
            .height(100.dp)
            .width(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(0.3f)
        ),
        onClick = onClick,
        shape = Shapes().medium,
        border = BorderStroke(
            width = if (isSelected) 4.dp else 1.dp,
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
                color = textColor,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp6))


            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.BlackWhiteColor,
                fontWeight = FontWeight.Bold
            )
        }
    }

}