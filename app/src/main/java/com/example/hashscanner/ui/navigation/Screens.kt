package com.example.hashscanner.ui.navigation

import kotlinx.serialization.Serializable

interface Screens {

    @Serializable
    object Authentication : Screens



    @Serializable
    object Landing : Screens


    @Serializable
    object Scan : Screens

    @Serializable
    object NoInternet : Screens

    @Serializable
    data class AppDetails(
        val packageName: String,
        val scanId: String? = null
    ) : Screens



    @Serializable
    data class AppList(
        val riskLevel: com.example.hashscanner.ui.ui_utils.RiskLevelsUI,
        val scanId: String? = null,
        val showSystem: Boolean = false
    ) : Screens


    @Serializable
    data class RiskLevelList(
        val scanId: String? = null
    ) : Screens


    @Serializable
    object ScanHistory : Screens

    @Serializable
    data class ScanHistoryDetails(
        val scanId: String? = null
    ) : Screens


}