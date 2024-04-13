package com.example.hotels.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
abstract class BaseBooking {
    var startDate: Instant? = null
    var endDate: Instant? = null
    var userId : Int? = null
    var guestCount : Int? = null
    var guestNames: String? = null
}