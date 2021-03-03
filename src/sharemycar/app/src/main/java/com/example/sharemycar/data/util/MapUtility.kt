package com.example.sharemycar.data.util

import android.content.Context
import android.graphics.Color
import com.example.sharemycar.R
import com.example.sharemycar.data.models.Route

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*

import com.google.maps.android.PolyUtil
import com.google.android.gms.maps.model.LatLng
import com.example.sharemycar.data.models.Spot


class MapsController(context: Context, googleMap: GoogleMap) {

    private val mContext: Context = context
    private val mGoogleMap: GoogleMap = googleMap

    private var mSpotMarkerList = ArrayList<Marker>()

    private var mRouteMarkerList = ArrayList<Marker>()
    private lateinit var mRoutePolyline: Polyline



    fun clearMarkers() {
        for (marker in mSpotMarkerList) {
            marker.remove()
        }
        mSpotMarkerList.clear()
    }

    fun setMarkersAndRoute(route: Route) {
        val startLatLng = LatLng(route.startLat!!, route.startLng!!)
        val startMarkerOptions: MarkerOptions = MarkerOptions().position(startLatLng).title(route.startName).icon(BitmapDescriptorFactory.fromBitmap(MapsFactory.drawMarker(mContext, "Moi",color = Color.BLUE)))
        val endLatLng = LatLng(route.endLat!!, route.endLng!!)
        val endMarkerOptions: MarkerOptions = MarkerOptions().position(endLatLng).title(route.endName).icon(BitmapDescriptorFactory.fromBitmap(MapsFactory.drawMarker(mContext, "Lui",Color.RED)))
        val startMarker = mGoogleMap.addMarker(startMarkerOptions)
        val endMarker = mGoogleMap.addMarker(endMarkerOptions)
        mRouteMarkerList.add(startMarker)
        mRouteMarkerList.add(endMarker)

        val polylineOptions = MapsFactory.drawRoute(mContext)
        val pointsList = PolyUtil.decode(route.overviewPolyline)
        for (point in pointsList) {
            polylineOptions.add(point)
        }

        mRoutePolyline = mGoogleMap.addPolyline(polylineOptions)

        mGoogleMap.animateCamera(MapsFactory.autoZoomLevel(mRouteMarkerList))
    }

    fun clearMarkersAndRoute() {
        for (marker in mRouteMarkerList) {
            marker.remove()
        }
        mRouteMarkerList.clear()

        if (::mRoutePolyline.isInitialized) {
            mRoutePolyline.remove()
        }
    }
}

