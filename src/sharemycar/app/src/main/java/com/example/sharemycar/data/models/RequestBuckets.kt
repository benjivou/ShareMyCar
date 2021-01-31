package com.example.sharemycar.data.models

import com.google.gson.annotations.SerializedName

/**
 * Containe the class used to preparea request
 */
data class LoginBucket(
    @SerializedName("username") var userName: String,
    @SerializedName("password") var userPassword: String
)

data class UserAuthentificaticator(
    @SerializedName("username") var userName: String,
    @SerializedName("password") var userPassword: String,
    @SerializedName("email") val username: String
)
