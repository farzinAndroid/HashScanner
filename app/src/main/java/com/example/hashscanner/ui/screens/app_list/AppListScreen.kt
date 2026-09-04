package com.example.hashscanner.ui.screens.app_list

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hashscanner.R
import com.example.hashscanner.ui.navigation.Screens
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.ui.ui_utils.AppTopBar

import com.example.hashscanner.viewmodel.AppDatabaseViewModel

@Composable
fun AppListScreen(
    navController: NavController,
    riskLevel: com.example.hashscanner.ui.ui_utils.RiskLevelsUI,
    scanId: String? = null,
    showSystem: Boolean = false,
    databaseViewModel: AppDatabaseViewModel
) {


    Scaffold(
        topBar = {
            AppTopBar(
                topBarText = stringResource(R.string.topbar_title_apps),
                onClick = {
                    navController.popBackStack()
                }
            )
        },
        content = {paddingValues ->
            AppListSection(
                paddingValues = paddingValues,
                initialRiskLevel = riskLevel,
                scanId = scanId,
                databaseViewModel = databaseViewModel,
                showSystem = showSystem,
                onAppClick = { packageName ->
                    navController.navigate(Screens.AppDetails(packageName, scanId))
                }
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AppListScreenPreview() {
    HashScannerTheme {
        AppListContent(
            paddingValues = androidx.compose.foundation.layout.PaddingValues(16.dp),
            currentApps = emptyList(),
            onAppClick = {},
            isReady = true
        )
    }
}
