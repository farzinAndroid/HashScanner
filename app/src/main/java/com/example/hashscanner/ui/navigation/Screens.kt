package com.example.hashscanner.ui.navigation

import kotlinx.serialization.Serializable

interface Screens {

    @Serializable
    object Landing : Screens


    @Serializable
    object Scan : Screens

    @Serializable
    object Details : Screens



    @Serializable
    object AppList : Screens



    /*
        @Serializable
        data class Playlists(
            val playlistId:Int,
            val playlistName:String
        ) : Screens*/


}