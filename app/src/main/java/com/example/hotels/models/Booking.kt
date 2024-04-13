package com.example.hotels.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable


@Serializable
class Booking () : java.io.Serializable{
    var id : Int? = null
    var startDate: Instant? = null
    var endDate: Instant? = null
    var userId : Int? = null
    var guestCount : Int? = null
    var guestNames: String? = null
    val room : Room = Room()
}