package com.example.sharemycar.data.retrofit



import com.example.sharemycar.data.models.Directions
import android.text.Layout
import com.example.sharemycar.data.untracked.PLACES_KEY
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

