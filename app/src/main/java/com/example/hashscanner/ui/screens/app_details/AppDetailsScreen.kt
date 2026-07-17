package com.example.hashscanner.ui.screens.app_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.hashscanner.R
import com.example.hashscanner.ui.theme.BackgroundColor
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.ui.theme.spacing
import com.example.hashscanner.ui.ui_utils.AppTopBar
import com.example.hashscanner.viewmodel.AppDatabaseViewmodel

@Composable
fun AppDetailsScreen(
    databaseViewmodel: AppDatabaseViewmodel = hiltViewModel(),
    packageName: String,
    navController: NavController
) {

    val appDetails by databaseViewmodel.appByPackage.collectAsStateWithLifecycle()

    LaunchedEffect(packageName) {
        databaseViewmodel.getAppByPackage(packageName)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.BackgroundColor,
        topBar = {
            AppTopBar(topBarText = stringResource(R.string.topbar_title_app_details))
        },
        bottomBar = { AppDetailsBottomBar() }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = MaterialTheme.spacing.dp16),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.dp16),
            contentPadding = PaddingValues(vertical = MaterialTheme.spacing.dp16)
        ) {
            appDetails?.let { app ->
                item { AppHeaderSection(appInfo = app) }
                item { RiskScoreCard(score = app.riskScore) }
                item { SuspiciousReasonsCard(appInfo = app) }
                item { TechnicalDetailsCard(appInfo = app) }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppDetailsScreenPreview() {
    HashScannerTheme {
        AppDetailsScreen(
            packageName = "com.example.hashscanner",
            navController = NavController(LocalContext.current)
        )
    }
}
