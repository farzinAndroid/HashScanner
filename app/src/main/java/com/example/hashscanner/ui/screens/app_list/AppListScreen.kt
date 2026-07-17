package com.example.hashscanner.ui.screens.app_list

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.hashscanner.R
import com.example.hashscanner.ui.navigation.Screens
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.ui.ui_utils.AppTopBar

@Composable
fun AppListScreen(
    navController: NavController
) {


    Scaffold(
        topBar = {
            AppTopBar(
                topBarText = stringResource(R.string.topbar_title_suspicious_apps),
                onClick = {
                    navController.popBackStack()
                }
            )
        },
        content = {paddingValues ->
            AppListSection(
                paddingValues = paddingValues,
                onAppClick = { packageName ->
                    navController.navigate(Screens.Details(packageName))
                }
            )
        }
    )

}

@Preview(showBackground = true)
@Composable
fun AppListScreenPreview() {
    HashScannerTheme {
        AppListScreen(navController = NavController(LocalContext.current))
    }
}
