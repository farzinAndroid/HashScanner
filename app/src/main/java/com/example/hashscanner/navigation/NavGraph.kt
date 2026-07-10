package com.example.hashscanner.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
            LandingPageScreen()
        }

        composable<Screens.Scan> {
            ScanScreen()
        }

    }


}