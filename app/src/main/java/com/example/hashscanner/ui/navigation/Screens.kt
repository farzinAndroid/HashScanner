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
    object AppList : Screens



    /*
        @Serializable
        data class Playlists(
            val playlistId:Int,
            val playlistName:String
        ) : Screens*/


}