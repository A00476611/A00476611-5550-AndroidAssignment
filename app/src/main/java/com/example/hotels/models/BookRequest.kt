package com.example.hotels.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
class BookRequest(
    var startDate: Instant,
    var endDate:Instant,
    var roomId:Int? = 0,
    var userId:Int? = 0,
    var guestCount:Int = 0,
    var guestNames: String,
) {}