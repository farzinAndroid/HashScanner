package com.example.hashscanner.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.example.hashscanner.data.model.api.ScanResultModel
import com.example.hashscanner.ui.screens.app_details.AppDetailsScreen
import com.example.hashscanner.ui.screens.app_list.AppListScreen
import com.example.hashscanner.ui.screens.authentication.AuthenticationScreen
import com.example.hashscanner.ui.screens.error.NoInternetScreen
import com.example.hashscanner.ui.screens.landing.LandingPageScreen
import com.example.hashscanner.ui.screens.risk_level_list.RiskLevelListScreen
import com.example.hashscanner.ui.screens.scan.ScanScreen
import com.example.hashscanner.ui.ui_utils.GlobalScanOverlay
import com.example.hashscanner.utils.Constants
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
    appDatabaseViewModel: AppDatabaseViewModel
) {


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    val isExcluded = listOf(
        Screens.NoInternet::class.simpleName,
        Screens.Authentication::class.simpleName,
        Screens.Landing::class.simpleName,
        Screens.Scan::class.simpleName,
    ).any { currentRoute?.contains(it.toString()) == true }


    LaunchedEffect(currentRoute) {
        if (currentRoute != null && !isExcluded) {
            scannerViewModel.getScanResult(ScanResultModel(Constants.DEVICE_ID))
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
                    onButtonClick = { navController.navigate(Screens.Scan) },
                    appViewModel = appViewModel
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
                    databaseViewModel = appDatabaseViewModel
                )
            }

            composable<Screens.RiskLevelList> {
                RiskLevelListScreen(
                    navController = navController,
                    databaseViewModel = appDatabaseViewModel
                )
            }

            composable<Screens.Details> { backStackEntry ->
                val details = backStackEntry.toRoute<Screens.Details>()
                AppDetailsScreen(
                    navController = navController,
                    packageName = details.packageName,
                    databaseViewModel = appDatabaseViewModel,
                    scannerViewModel = scannerViewModel
                )
            }
        }


        GlobalScanOverlay(scannerViewModel = scannerViewModel, navController = navController)
    }
}
