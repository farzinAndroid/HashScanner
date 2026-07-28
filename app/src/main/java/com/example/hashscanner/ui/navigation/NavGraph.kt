package com.example.hashscanner.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.hashscanner.ui.screens.app_details.AppDetailsScreen
import com.example.hashscanner.ui.screens.app_list.AppListScreen
import com.example.hashscanner.ui.screens.authentication.AuthenticationScreen
import com.example.hashscanner.ui.screens.error.NoInternetScreen
import com.example.hashscanner.ui.screens.landing.LandingPageScreen
import com.example.hashscanner.ui.screens.risk_level_list.RiskLevelListScreen
import com.example.hashscanner.ui.screens.scan.ScanScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: Screens,
    isChecking: Boolean,
    onRetry: () -> Unit
) {

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable<Screens.NoInternet> {
            NoInternetScreen(
                isChecking = isChecking,
                onRetry = onRetry
            )
        }

        composable<Screens.Authentication> {
            AuthenticationScreen(
                navController = navController
            )
        }

        composable<Screens.Landing> {
            LandingPageScreen(
                onButtonClick = {
                    navController.navigate(Screens.Scan)
                }
            )
        }

        composable<Screens.Scan> {
            ScanScreen(
                navController = navController
            )
        }

        composable<Screens.AppList> { backStackEntry ->
            val appList = backStackEntry.toRoute<Screens.AppList>()
            AppListScreen(
                navController = navController,
                riskLevel = appList.riskLevel
            )
        }

        composable<Screens.RiskLevelList> {
            RiskLevelListScreen(navController = navController)
        }

        composable<Screens.Details> { backStackEntry ->
            val details = backStackEntry.toRoute<Screens.Details>()
            AppDetailsScreen(
                navController = navController,
                packageName = details.packageName
            )
        }
    }
}
