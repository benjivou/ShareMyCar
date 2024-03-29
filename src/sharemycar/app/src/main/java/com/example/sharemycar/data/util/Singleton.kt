package com.example.sharemycar.data.util

import com.example.sharemycar.data.retrofit.GoogleMethods
import com.example.sharemycar.data.retrofit.StartProcessService
import com.example.sharemycar.data.retrofit.UserService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

 const val URL_API = "http://192.168.43.196:8080/api/"
//const val URL_API = "http://192.168.43.137:8080/api/"

object Singleton {
    private const val GOOGLE_BASE_URL = "https://maps.googleapis.com/maps/api/"

   private val localRetrofit = Retrofit.Builder()
        .baseUrl(URL_API)
        .addConverterFactory(GsonConverterFactory.create())

        .build()

    val userService = localRetrofit.create(UserService::class.java)!!
    val sessionsService = localRetrofit.create(StartProcessService::class.java)!!
    val googleService = Retrofit.Builder()
        .baseUrl(GOOGLE_BASE_URL)
        .client(OkHttpClient().newBuilder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(GoogleMethods::class.java)


}