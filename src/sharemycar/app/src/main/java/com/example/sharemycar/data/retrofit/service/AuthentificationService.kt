package com.example.sharemycar.data.retrofit.service

import com.example.sharemycar.data.models.User
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
data class LoginBucket(@SerializedName("username") var userName: String,   @SerializedName("password") var userPassword: String,)
data class UserAuthentificaticator(
    @SerializedName("username") var userName: String,
    @SerializedName("password") var userPassword: String,
    @SerializedName("email") val username: String
)

