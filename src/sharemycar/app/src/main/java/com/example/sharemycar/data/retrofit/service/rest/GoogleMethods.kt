package com.example.sharemycar.data.retrofit.service.rest


import android.text.Layout
import com.example.sharemycar.data.models.Directions
import com.example.sharemycar.data.models.NearbySearch
import com.google.android.gms.maps.model.LatLng

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMethods {


    // Google Directions API -- directions
    @GET("directions/json")
    fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") key: String
    ): Call<Directions>
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