package com.example.hotels.models

import kotlinx.serialization.Serializable

@Serializable
class User(
    val id: Int,
    var name: String,
    var email: String,
    var password: String
) {}