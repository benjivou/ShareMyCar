package com.example.sharemycar.data.retrofit.service.rest

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface StartProcessService {
    @POST("sessions")
    fun startProcess(
        @Body matchObject: MatchObject,
    ): Call<MatchObject>
}

data class MatchObject(
    @SerializedName("requester") val requester: RequesterTypeEnum,
    @SerializedName("user") val user: UserStartProcess,
    @SerializedName("dest") val dest: PositionLatLong,
    @SerializedName("position") val position: PositionLatLong,
    @SerializedName("maxDist") val maxDist: Int?,
)

enum class RequesterTypeEnum {
    @SerializedName("passenger") PASSENGER,
    @SerializedName("driver") DRIVER
}

data class UserStartProcess(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String
)
data class PositionLatLong(
    @SerializedName("latitude") var latitude: Double,
    @SerializedName("longitude") var longitude: Double
)

