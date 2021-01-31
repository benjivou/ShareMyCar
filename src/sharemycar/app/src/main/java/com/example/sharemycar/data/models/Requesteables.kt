package com.example.sharemycar.data.models

import com.google.gson.annotations.SerializedName

class User(
    @SerializedName("id") val id: Long,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
)

