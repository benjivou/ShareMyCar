package com.example.sharemycar

import android.app.Application
import com.example.sharemycar.data.untracked.PLACES_KEY
import com.google.android.libraries.places.api.Places

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize the SDK
        Places.initialize(applicationContext, PLACES_KEY)

        // Create a new PlacesClient instance
        val placesClient = Places.createClient(this)
    }
}