package com.example.sharemycar.data.models

import com.google.gson.annotations.SerializedName

data class Directions(@SerializedName("routes")
                      val routes: List<RoutesItem>,
                      @SerializedName("geocoded_waypoints")
                      val geocodedWaypoints: List<GeocodedWaypointsItem>?,
                      @SerializedName("status")
                      val status: String = "")