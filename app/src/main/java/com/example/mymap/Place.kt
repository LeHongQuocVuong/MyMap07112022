package com.example.mymap

import java.io.Serializable

data class Place(
    val title:String,
    val decscription: String,
    val latitude: Double,
    val longitude: Double
):Serializable
