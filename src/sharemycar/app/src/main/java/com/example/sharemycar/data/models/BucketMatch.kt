package com.example.sharemycar.data.models;

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName;

data class BucketMatch(
    @SerializedName("id") val id:Long,
    @SerializedName("name") val name:String
)
data class Bucket1Match(
    @SerializedName("pub") val idPub:String,
    @SerializedName("sub") val idSub:String
)

