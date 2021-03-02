package com.example.sharemycar.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.sharemycar.data.displayabledata.DataPreprared
import com.example.sharemycar.data.displayabledata.ErrorDataPreprared
import com.example.sharemycar.data.displayabledata.SuccessDataPreprared
import com.example.sharemycar.data.models.Bucket1Match
import com.example.sharemycar.data.models.Directions
import com.example.sharemycar.data.models.Route
import com.example.sharemycar.data.mqtt.ErrorMQTTPreprared
import com.example.sharemycar.data.mqtt.MqttCommunicator
import com.example.sharemycar.data.mqtt.SuccessMQTTPreprared
import com.example.sharemycar.data.untracked.PLACES_KEY
import com.example.sharemycar.data.util.Singleton
import com.example.sharemycar.ui.livedata.LocationLiveData
import com.example.sharemycar.ui.livedata.MQTTLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import convertLatLng
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import splitties.toast.toast


private const val TAG = "MapsViewModels"

class MapsViewModels(val context: Context, dest: LatLng, val userId: Long) : ViewModel() {

    private var mqttPub: MqttCommunicator = MqttCommunicator(context, userId)
    private lateinit var mqttClient: MQTTLiveData
    private var pubTopic = "positions"

    val isThisTheEnd = MutableLiveData(false)


    private val _pathToTarget = MediatorLiveData<DataPreprared<Route>?>()
    val pathToTarget: LiveData<DataPreprared<Route>?>
        get() {
            return _pathToTarget
        }

    private val _myLocation = LocationLiveData(context)
    private val _locationDestData = MutableLiveData<LatLng>(dest)

    override fun onCleared() {
        super.onCleared()

        mqttPub.disconnect()
    }

    init {

        // when the user sensor change
        _pathToTarget.addSource(_myLocation, Observer {
            _locationDestData.value?.run {
                positionDestChange(it, this)
                if (mqttPub.isConnected) mqttPub.publish(
                    pubTopic,
                    "$userId/{\"latitude\":${it.latitude},\"longitude\":${it.longitude}}"
                )
            } ?: toast("Pas de destination")
        }

        )
        /*_pathToTarget.addSource(_locationDestData, Observer {
            _myLocation.value?.run {
                positionUserChange(this, it)
            }

        })*/

    }

    fun positionUserChange(origin: LatLng, dest: LatLng) {
        positionDestChange(origin, dest)
    }


    fun positionDestChange(origin: LatLng, dest: LatLng) {
        val directionsCall = Singleton.googleService.getDirections(
            origin.convertLatLng(),
            dest.convertLatLng(),
            PLACES_KEY
        )
        Log.i(TAG, "positionChange: ${directionsCall.request()}")

        directionsCall.enqueue(object : Callback<Directions> {
            override fun onResponse(
                call: Call<Directions>,
                response: Response<Directions>
            ) {
                val directions = response.body()!!

                if (directions.status.equals("OK")) {
                    val legs = directions.routes[0].legs[0]
                    val route = Route(
                        "Moi",
                        "Destination",
                        legs.startLocation.lat,
                        legs.startLocation.lng,
                        legs.endLocation.lat,
                        legs.endLocation.lng,
                        directions.routes[0].overviewPolyline.points
                    )
                    _pathToTarget.value = SuccessDataPreprared(route)
                } else {
                    _pathToTarget.value = ErrorDataPreprared(0, directions.status)
                }

            }

            override fun onFailure(call: Call<Directions>, t: Throwable) {
                _pathToTarget.value = ErrorDataPreprared(0, t.toString())
            }
        })
    }

    fun changeListener(topic: Bucket1Match) {

        // this.mqttStartFollow = MQTTLiveData(context, topic.idSub, userId.toString())

        pubTopic = topic.idPub
        mqttClient = MQTTLiveData(context, topic.idSub, userId.toString())
        _pathToTarget.addSource(mqttClient) {
            when (it) {
                is ErrorMQTTPreprared -> {
                    Log.d(TAG, "changeListener: issue")
                }
                is SuccessMQTTPreprared ->
                    try {
/*
                        if (it.content == "end") {
                            _pathToTarget.removeSource(_locationDestData)
                            _pathToTarget.removeSource(_myLocation)
                            _pathToTarget.removeSource(mqttClient)
                        }*/

                        _myLocation.value?.run {
                            var msg = it.content.split("/")[1]
                            var map: Map<String, Double> = HashMap()
                            map = Gson().fromJson(msg, map.javaClass)

                            // calcul the distance to the point
                            /*val loc1 = Location(LocationManager.GPS_PROVIDER)
                            val loc2 = Location(LocationManager.GPS_PROVIDER)*/
                            /*  loc1.latitude = map["latitude"]!!
                              loc1.longitude = map["longitude"]!!
                              loc2.latitude = _myLocation.value!!.latitude
                              loc2.longitude = _myLocation.value!!.longitude
                              val distance = loc1.distanceTo(loc2)*/
                            /* if (distance < 5) {
                                 _pathToTarget.removeSource(_locationDestData)
                                 _pathToTarget.removeSource(_myLocation)
                                 _pathToTarget.removeSource(mqttClient)
                                 mqttPub.publish(
                                     pubTopic,
                                     "end",
                                     qos = 1,
                                 )
                                 isThisTheEnd.value = true*/

                            _locationDestData.value =
                                LatLng(map["latitude"]!!, map["longitude"]!!)


                        }
                    } catch (e: Exception) {
                        Log.w(TAG, "changeListener: ${e.stackTrace}", e)
                    }


            }
        }


    }


}
