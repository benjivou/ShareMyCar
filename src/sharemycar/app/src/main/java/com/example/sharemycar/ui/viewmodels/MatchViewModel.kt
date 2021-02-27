package com.example.sharemycar.ui.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sharemycar.data.models.BucketMatch
import com.example.sharemycar.data.models.User
import com.example.sharemycar.data.mqtt.MqttCommunicator
import com.example.sharemycar.data.retrofit.MatchObject
import com.example.sharemycar.ui.livedata.MQTTLiveData
import com.google.gson.Gson

class MatchViewModel(ctx: Context, val user: User) : ViewModel() {

    val driver: MutableLiveData<User> = MutableLiveData(null)
    val passenger: MutableLiveData<User> = MutableLiveData(null)
    private lateinit var mqttPub: MqttCommunicator

    init {
        mqttPub = MqttCommunicator(ctx, user.id)
    }

    val mqttIdTopicLiveData = MQTTLiveData(ctx, "matches", user.id.toString())
    fun sendAnswer(isAccepted: Boolean, matchObject: BucketMatch) {
        val type: String =
            if (isAccepted) "accept"
            else {
                "refuse"
            } + "/"

        mqttPub.publish("matches", "$type${Gson().toJson(matchObject)}")
    }
}