package com.example.sharemycar.data.retrofit.service

import com.example.sharemycar.data.models.User
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*

interface UserService {
    @POST("/users/login")
    fun loginPost(
        @Body userAuthentifiator: UserAuthentificaticator
    ): Call<User>

    @POST("/users/new")
    fun registerPost(
        @Body userAuthentifiator: UserAuthentificaticator
    ): Call<RequestMessage>
}

data class UserAuthentificaticator(
    @SerializedName("username") var userID: String,
    @SerializedName("password") var userPassword: String
)

data class RequestMessage(
    @SerializedName("message") var message: String,
)