package com.example.hashscanner.ui.screens.app_list

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hashscanner.ui.theme.BackgroundColor
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.viewmodel.AppDatabaseViewmodel

@Composable
fun AppListSection(
    paddingValues: PaddingValues,
    databaseViewmodel: AppDatabaseViewmodel = hiltViewModel()
) {

    val highRiskAppsCount by databaseViewmodel.highRiskAppsCount.collectAsStateWithLifecycle()
    val mediumSusAppsCount by databaseViewmodel.mediumRiskAppsCount.collectAsStateWithLifecycle()
    val lowSusAppsCount by databaseViewmodel.lowRiskAppsCount.collectAsStateWithLifecycle()

    val lowRiskApps by databaseViewmodel.lowRiskApps.collectAsStateWithLifecycle()
    val mediumRiskApps by databaseViewmodel.mediumRiskApps.collectAsStateWithLifecycle()
    val highRiskApps by databaseViewmodel.highRiskApps.collectAsStateWithLifecycle()


    LaunchedEffect(true) {
        databaseViewmodel.countHighRiskApps()
        databaseViewmodel.countMediumRiskApps()
        databaseViewmodel.countLowRiskApps()
    }
    LaunchedEffect(true) {
        databaseViewmodel.getLowRiskApps()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.BackgroundColor)
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),

            ) {

            item {
                SuspiciousBadgeSection(
                    highRiskAppsCount = highRiskAppsCount,
                    mediumRiskAppsCount = mediumSusAppsCount,
                    lowRiskAppsCount = lowSusAppsCount
                )
            }

            items(lowRiskApps){
                Log.e("TAG",it.riskLevel)
                AppCard(it)
            }

        }


    }

}

@Preview(showBackground = true)
@Composable
fun AppListSectionPreview() {
    HashScannerTheme {
        AppListSection(
            paddingValues = PaddingValues()
        )
    }
}
