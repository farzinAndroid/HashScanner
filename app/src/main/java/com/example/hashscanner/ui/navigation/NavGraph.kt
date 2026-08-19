package com.example.hashscanner.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.example.hashscanner.ui.screens.app_details.AppDetailsScreen
import com.example.hashscanner.ui.screens.app_list.AppListScreen
import com.example.hashscanner.ui.screens.authentication.AuthenticationScreen
import com.example.hashscanner.ui.screens.error.NoInternetScreen
import com.example.hashscanner.ui.screens.history.ScanHistoryScreen
import com.example.hashscanner.ui.screens.history_details.HistoryDetailsScreen
import com.example.hashscanner.ui.screens.landing.LandingPageScreen
import com.example.hashscanner.ui.screens.risk_level_list.RiskLevelListScreen
import com.example.hashscanner.ui.screens.scan.ScanScreen
import com.example.hashscanner.ui.ui_utils.GlobalScanOverlay
import com.example.hashscanner.viewmodel.AppDatabaseViewModel
import com.example.hashscanner.viewmodel.AppViewModel
import com.example.hashscanner.viewmodel.ScannerViewModel


@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: Screens,
    isChecking: Boolean,
    onRetry: () -> Unit,
    appViewModel: AppViewModel,
    scannerViewModel: ScannerViewModel,
    appDatabaseViewModel: AppDatabaseViewModel,
    paddingValues: PaddingValues
) {


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    val isExcluded = listOf(
        Screens.NoInternet::class.simpleName,
        Screens.Authentication::class.simpleName,
        Screens.Scan::class.simpleName,
    ).any { currentRoute?.contains(it.toString()) == true }


    LaunchedEffect(currentRoute) {
        if (currentRoute != null && !isExcluded) {
            val scanIdFromRoute = if (currentRoute.contains(Screens.HistoryDetails::class.simpleName.toString())) {
                navBackStackEntry?.toRoute<Screens.HistoryDetails>()?.scanId
            } else null
            
            scannerViewModel.getScanResult(scanIdFromRoute)
        } else {
            scannerViewModel.stopPolling()
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {

        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            composable<Screens.NoInternet> {
                NoInternetScreen(isChecking = isChecking, onRetry = onRetry)
            }

            composable<Screens.Authentication> {
                AuthenticationScreen(
                    navController = navController,
                    scannerViewModel = scannerViewModel,
                    appViewModel = appViewModel
                )
            }

            composable<Screens.Landing> {
                LandingPageScreen(
                    onScanClick = {
                        scannerViewModel.resetScanState()
                        navController.navigate(Screens.Scan)
                    },
                    onHistoryClick = { navController.navigate(Screens.ScanHistory) },
                    appViewModel = appViewModel,
                    databaseViewModel = appDatabaseViewModel,
                    navController = navController
                )
            }

            composable<Screens.ScanHistory> {
                ScanHistoryScreen(
                    onBackClick = { navController.popBackStack() },
                    databaseViewModel = appDatabaseViewModel,
                    navController = navController
                )
            }

            composable<Screens.Scan> {
                ScanScreen(
                    navController = navController,
                    scannerViewModel = scannerViewModel,
                    appDatabaseViewModel = appDatabaseViewModel
                )
            }

            composable<Screens.AppList> { backStackEntry ->
                val appList = backStackEntry.toRoute<Screens.AppList>()
                AppListScreen(
                    navController = navController,
                    riskLevel = appList.riskLevel,
                    scanId = appList.scanId,
                    databaseViewModel = appDatabaseViewModel
                )
            }

            composable<Screens.RiskLevelList> { backStackEntry ->
                val riskLevelList = backStackEntry.toRoute<Screens.RiskLevelList>()
                RiskLevelListScreen(
                    navController = navController,
                    scanId = riskLevelList.scanId,
                    databaseViewModel = appDatabaseViewModel
                )
            }

            composable<Screens.Details> { backStackEntry ->
                val details = backStackEntry.toRoute<Screens.Details>()
                AppDetailsScreen(
                    navController = navController,
                    packageName = details.packageName,
                    scanId = details.scanId,
                    databaseViewModel = appDatabaseViewModel,
                    scannerViewModel = scannerViewModel
                )
            }

            composable<Screens.HistoryDetails> { backStackEntry ->
                val details = backStackEntry.toRoute<Screens.HistoryDetails>()
                HistoryDetailsScreen(
                    navController = navController,
                    scanId = details.scanId,
                    databaseViewModel = appDatabaseViewModel,
                    scannerViewModel = scannerViewModel
                )
            }


        }

        GlobalScanOverlay(scannerViewModel = scannerViewModel, navController = navController)
    }
}
