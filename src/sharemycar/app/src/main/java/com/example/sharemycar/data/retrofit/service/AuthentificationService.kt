package com.example.sharemycar.data.retrofit.service

import androidx.lifecycle.MutableLiveData
import com.example.sharemycar.data.models.User
import com.example.sharemycar.data.retrofit.ApiResponse
import com.google.gson.annotations.SerializedName
import retrofit2.http.*

interface UserService {
    @Headers("Content-Type: application/json")
    @POST("login")
    fun loginPost(
        @Body userAuthentifiator: UserAuthentificaticator
    ): MutableLiveData<ApiResponse<User>>

    @Headers("Content-Type: application/json")
    @POST("new")
    fun registerPost(
        @Body userAuthentifiator: UserAuthentificaticator
    ): MutableLiveData<ApiResponse<RequestMessage>>
}

data class UserAuthentificaticator(
    @SerializedName("username") var userID: String,
    @SerializedName("password") var userPassword: String
)

data class RequestMessage(
    @SerializedName("message") var message: String,
)