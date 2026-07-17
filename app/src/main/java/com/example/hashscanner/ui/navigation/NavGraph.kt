package com.example.hashscanner.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.hashscanner.ui.screens.app_details.AppDetailsScreen
import com.example.hashscanner.ui.screens.app_list.AppListScreen
import com.example.hashscanner.ui.screens.landing.LandingPageScreen
import com.example.hashscanner.ui.screens.scan.ScanScreen

@Composable
fun NavGraph(
    navController: NavHostController
) {


    NavHost(
        navController = navController,
        startDestination = Screens.Landing
    ) {

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



        composable<Screens.AppList> {
            AppListScreen(navController = navController)
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