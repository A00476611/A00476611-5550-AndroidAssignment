package com.example.hotels.models

import kotlinx.serialization.Serializable

@Serializable
class Room : java.io.Serializable {
    val id = 0
    val name: String? = null
    val bedCount = 0
    val hotel = Hotel()
    val price = 0
    val image :String? = null
}