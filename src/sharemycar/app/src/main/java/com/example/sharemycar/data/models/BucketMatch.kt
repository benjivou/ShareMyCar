package com.example.sharemycar.data.models;

import com.google.gson.annotations.SerializedName;

data class BucketMatch(
    @SerializedName("driver")
    val driver: User?,
    @SerializedName("passenger")
    val passenger: User?
)