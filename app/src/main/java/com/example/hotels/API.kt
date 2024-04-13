package com.example.hotels

import androidx.annotation.Nullable
import androidx.lifecycle.lifecycleScope
import com.example.hotels.models.BookRequest
import com.example.hotels.models.Booking
import com.example.hotels.models.Login
import com.example.hotels.models.Search
import com.example.hotels.models.User
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException


class API {
    private val url = "https://book-me-api.azurewebsites.net"
    private val client = OkHttpClient()

    public fun login(login: Login, callback: Callback){
        post("/user/login", Json.encodeToString(login), callback)
    }

    public fun register(user: User, callback: Callback){
        put("/user", Json.encodeToString(user), callback)

    }

    public fun searchRooms(search: Search, callback: Callback){
        post("/room/search", Json.encodeToString(search), callback)
    }

    public fun book(booking: BookRequest, callback: Callback){
        post("/room/book", Json.encodeToString(booking), callback)
    }

    public fun getUserBookings(userId:Int, callback: Callback){
        get("/user/$userId/bookings", callback)
    }

    public fun  updateUser(user: User, callback: Callback){
        post("/user", Json.encodeToString(user), callback)
    }

    public fun deleteBooking(bookingId:Int, callback: Callback){
        delete("/booking/${bookingId}", callback)
    }
    public fun get(endpoint:String, queries:List<Pair<String,String>>, callback: Callback){
        var queryString = "?"
        for(query in queries){
            queryString = queryString.plus("${query.first}=${query.second}")
        }
        get("${endpoint}${queryString}", callback)

    }
    public fun get(endpoint:String, callback: Callback){
        val request = Request.Builder()
            .url("${url}${endpoint}")
            .get()
            .build()
        client.newCall(request).enqueue(callback)
    }

    public fun post(endpoint:String, body:String, callback: Callback)  {
        val JSON = MediaType.parse("application/json; charset=utf-8")
        val requestBody = RequestBody.create(JSON, body)
        val request = Request.Builder()
            .url("${url}${endpoint}")
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(callback)
    }

    public fun put(endpoint:String, body:String, callback: Callback)  {
        val JSON = MediaType.parse("application/json; charset=utf-8")
        val requestBody = RequestBody.create(JSON, body)
        val request = Request.Builder()
            .url("${url}${endpoint}")
            .put(requestBody)
            .build()
        client.newCall(request).enqueue(callback)
    }

    public fun delete(endpoint:String, callback: Callback)  {
        val request = Request.Builder()
            .url("${url}${endpoint}")
            .delete()
            .build()
        client.newCall(request).enqueue(callback)
    }
}