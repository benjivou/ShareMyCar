package com.example.sharemycar.data.util

import com.example.sharemycar.data.retrofit.service.UserService
import com.example.sharemycar.ui.livedata.LiveDataCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val URL_API = "http://192.168.1.70:8080/api/"

object Singleton {

   private  val retrofit = Retrofit.Builder()
        .baseUrl(URL_API )
        .addConverterFactory(GsonConverterFactory.create())
      //  .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .build()
    val userService = retrofit.create(UserService::class.java)!!

}