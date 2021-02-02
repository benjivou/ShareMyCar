package com.example.sharemycar.data.retrofit

import com.example.sharemycar.data.models.LoginBucket
import com.example.sharemycar.data.models.User
import com.example.sharemycar.data.models.UserAuthentificaticator
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*

interface UserService {
    @POST("users/login")
    fun loginPost(
        @Body loginBucket: LoginBucket,
    ): Call<User>

    @POST("users/new")
    fun registerPost(
        @Body userAuthentifiator: UserAuthentificaticator
    ): Call<User>
}

