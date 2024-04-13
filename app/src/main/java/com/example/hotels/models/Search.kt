package com.example.hotels.models

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
class Search(
    val startDate: Instant,
    val endDate: Instant,
    val bedCount: Int
) {}