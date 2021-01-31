package com.example.sharemycar.data.retrofit.service.rest

import android.text.Layout
import com.example.sharemycar.data.untracked.PLACES_KEY
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMethods {
    @GET("directions/json")
    fun getDirections(
        @Query(value = "origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") key: String = PLACES_KEY
    ): Call<Layout.Directions>
}

data class Route(
    val startName: String = "",
    val endName: String = "",
    val startLat: Double?,
    val startLng: Double?,
    val endLat: Double?,
    val endLng: Double?,
    val overviewPolyline: String = ""
)