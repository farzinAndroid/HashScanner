package com.example.hashscanner.ui.navigation

import kotlinx.serialization.Serializable

interface Screens {

    @Serializable
    object Home : Screens


    @Serializable
    object Details : Screens


    /*
        @Serializable
        data class Playlists(
            val playlistId:Int,
            val playlistName:String
        ) : Screens*/


}