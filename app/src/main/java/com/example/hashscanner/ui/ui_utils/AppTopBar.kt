package com.example.hashscanner.ui.ui_utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hashscanner.R
import com.example.hashscanner.ui.theme.AccentPurpleColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar() {


    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.AccentPurpleColor
        ),
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Done,
                    modifier = Modifier
                        .size(18.dp),
                    contentDescription = "",
                    tint = Color.Red
                )

                Text(
                    text = stringResource(R.string.topbar_title_scan_apps),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )

                Icon(
                    imageVector = Icons.Default.Done,
                    modifier = Modifier
                        .size(18.dp),
                    contentDescription = "",
                    tint = Color.Red
                )



            }
        },
    )


}

@Preview
@Composable
fun AppTopBarPrev() {
    AppTopBar()
}