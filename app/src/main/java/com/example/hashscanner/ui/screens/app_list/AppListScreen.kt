package com.example.hashscanner.ui.screens.app_list

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.hashscanner.R
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.ui.ui_utils.AppTopBar

@Composable
fun AppListScreen() {


    Scaffold(
        topBar = {
            AppTopBar(
                topBarText = stringResource(R.string.topbar_title_suspicious_apps),
            )
        },
        content = {paddingValues ->
            AppListSection(
                paddingValues = paddingValues
            )
        }
    )

}

@Preview(showBackground = true)
@Composable
fun AppListScreenPreview() {
    HashScannerTheme {
        AppListScreen()
    }
}
