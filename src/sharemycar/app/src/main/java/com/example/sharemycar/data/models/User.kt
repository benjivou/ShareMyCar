package com.example.sharemycar.data.models

import com.google.gson.annotations.SerializedName

class User(
    @SerializedName("id") val idController: Int,
    @SerializedName("username") val username: String
)