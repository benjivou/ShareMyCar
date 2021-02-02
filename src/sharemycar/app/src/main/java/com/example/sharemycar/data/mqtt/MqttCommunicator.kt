package com.example.sharemycar.data.mqtt

import android.content.Context
import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.sharemycar.data.models.User
import com.example.sharemycar.ui.viewmodels.MatchViewModel
import com.example.sharemycar.ui.viewmodels.SessionViewModel
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

var connectionFailure: Int = 0;

class MqttCommunicator {
    private lateinit var mqttClient: MqttAndroidClient
    private val SERVER_URL: String = "tcp://broker.emqx.io:1883" //"tcp://192.168.1.148:1883"
    private val TAG = "AndroidMqttClient"
    private var subscribedTopics: MutableList<String> = ArrayList()
    private var userId: Long = -1
    private lateinit var matchViewModel: MatchViewModel

    constructor(ctx: Context, id: Long, viewModel: MatchViewModel) {
        userId = id
        matchViewModel = viewModel
        connect(ctx)
    }

    fun connect(context: Context) {
        mqttClient = MqttAndroidClient(context, SERVER_URL, "ShareMyCarClient-${userId}")
        mqttClient.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {
                Log.d(TAG, "Connection lost ${cause.toString()}")
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                Log.d(TAG, "Receive message: ${message.toString()} from topic: $topic")
                handleMessage(topic!!, message.toString())
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                TODO("Not yet implemented")
            }

        })

        val options = MqttConnectOptions()
        try {
            mqttClient.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "Connection success")
                    subscribe("$userId")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Connection failure")
                    connectionFailure += 1
                    if(connectionFailure < 2) {
                        Log.d(TAG, "Retrying to connect...")
                        connect(context)
                    }
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun subscribe(topic: String, qos: Int = 1) {
        Log.d(TAG, "TRYING TO SUBSCRIBE")
        try {
            mqttClient.subscribe(topic, qos, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "Subscribed to $topic")
                    subscribedTopics.add(topic)
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Failed to subscribe $topic")
                }

            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun publish(topic: String, msg: String, qos: Int = 1, retained: Boolean = false) {
        try {
            val message = MqttMessage()
            message.payload = msg.toByteArray()
            message.qos = qos
            message.isRetained = retained
            mqttClient.publish(topic, message, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "$msg published to $topic")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Failed to publish $msg to $topic")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun unsubscribeToAll() {
        subscribedTopics.forEach { topic ->
            try {
                mqttClient.unsubscribe(topic, null, object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        Log.d(TAG, "Unsubscribed to $topic")
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Log.d(TAG, "Failed to unsubscribe $topic")
                    }
                })
            } catch (e: MqttException) {
                e.printStackTrace()
            }
        }
    }

    fun disconnect() {
        unsubscribeToAll()
        try {
            mqttClient.disconnect(null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "Disconnected")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Failed to disconnect")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun handleMessage(topic: String, msg: String) {
        if(topic == "$userId") {
            if (msg.trim() == "accept") {
                // The match is accepted
                Log.d(TAG, "THE MATCH IS ACCEPTED")
            } else if (msg.trim() == "refuse") {
                // The match is refuse
                Log.d(TAG, "THE MATCH IS REFUSED")
            } else {
                // convert match to JSON object
                val parser: JsonParser = JsonParser()
                val matchJSON: JsonObject = parser.parse(msg.trim()) as JsonObject

                // Save data of the user in MatchViewModel
                saveMatch(matchJSON)
            }
        }
    }

    private fun saveMatch(match: JsonObject) {
        val driverUserJson = match.getAsJsonObject("driver").getAsJsonObject("user")
        val driver: User = User(driverUserJson.getAsJsonPrimitive("id").asLong,
                                driverUserJson.getAsJsonPrimitive("username").asString,
                                driverUserJson.getAsJsonPrimitive("email").asString)


        val passengerUserJson = match.getAsJsonObject("passenger").getAsJsonObject("user")
        val passenger: User = User(passengerUserJson.getAsJsonPrimitive("id").asLong,
                                    passengerUserJson.getAsJsonPrimitive("username").asString,
                                    passengerUserJson.getAsJsonPrimitive("email").asString)

        matchViewModel.driver.value = driver
        matchViewModel.passenger.value = passenger
    }

}