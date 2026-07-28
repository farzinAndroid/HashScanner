package com.example.hashscanner.ui.navigation

import kotlinx.serialization.Serializable

interface Screens {

    @Serializable
    object Landing : Screens


    @Serializable
    object Scan : Screens

    @Serializable
    data class Details(
        val packageName:String
    ) : Screens



    @Serializable
    data class AppList(
        val riskLevel: com.example.hashscanner.ui.ui_utils.RiskLevelsUI
    ) : Screens


    @Serializable
    object RiskLevelList : Screens



    /*
        @Serializable
        data class Playlists(
            val playlistId:Int,
            val playlistName:String
        ) : Screens*/


}