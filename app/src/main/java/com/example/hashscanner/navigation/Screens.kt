package com.example.hashscanner.navigation

import kotlinx.serialization.Serializable

interface Screens {

    @Serializable
    object Landing : Screens


    @Serializable
    object Scan : Screens

    @Serializable
    object Details : Screens


    /*
        @Serializable
        data class Playlists(
            val playlistId:Int,
            val playlistName:String
        ) : Screens*/


}