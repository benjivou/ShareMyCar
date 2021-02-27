package com.example.sharemycar.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.sharemycar.data.displayabledata.DataPreprared
import com.example.sharemycar.data.displayabledata.ErrorDataPreprared
import com.example.sharemycar.data.displayabledata.SuccessDataPreprared
import com.example.sharemycar.data.models.Directions
import com.example.sharemycar.data.models.Route
import com.example.sharemycar.data.mqtt.MqttCommunicator
import com.example.sharemycar.data.retrofit.MatchObject
import com.example.sharemycar.data.untracked.PLACES_KEY
import com.example.sharemycar.data.util.Singleton
import com.example.sharemycar.ui.livedata.LocationLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import convertLatLng
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import splitties.toast.toast

private const val TAG = "MapsViewModels"

class MapsViewModels(context: Context, dest: LatLng, userId: Long) : ViewModel() {
    private val mInterval = 5000L // 5 seconds by default, can be changed later
    private val SERVER_URL: String = "tcp://broker.emqx.io:1883"
    private lateinit var mqttClient: MqttCommunicator
    private lateinit var mqttPub: MqttCommunicator



    private val _pathToTarget = MediatorLiveData<DataPreprared<Route>?>()
    val pathToTarget: LiveData<DataPreprared<Route>?>
        get() {
            return _pathToTarget
        }

    private val _myLocation = LocationLiveData(context)
    private val _locationDestData = MutableLiveData<LatLng>(dest)

    override fun onCleared() {
        super.onCleared()
        mqttClient.disconnect()
        mqttPub.disconnect()
    }

    init {
        mqttClient = MqttCommunicator(context, userId, userId.toString())
        mqttPub = MqttCommunicator(context, userId)
        // when the user sensor change
        _pathToTarget.addSource(_myLocation, Observer {
            _locationDestData.value?.run {
                positionDestChange(it, this)
                if (mqttPub.isConnected) mqttPub.publish(
                    "positions",
                    "$userId/{\"latitude\":${it.latitude},\"longitude\":${it.longitude}}"
                )
            } ?: toast("Pas de destination")
        }

        )
        _pathToTarget.addSource(_locationDestData, Observer {
            _myLocation.value?.run {
                positionUserChange(this, it)

            }

        })

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

}
