package com.example.sharemycar.data.mqtt

import android.content.Context
import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

var connectionFailure: Int = 0;

class MqttCommunicator(ctx: Context, val userId: Long, val topic: String="", val sub: Boolean = true) {
    private lateinit var mqttClient: MqttAndroidClient
    private val SERVER_URL: String = "tcp://broker.emqx.io:1883" //"tcp://192.168.1.148:1883"
    private val TAG = "AndroidMqttClient"
    private var subscribedTopics: MutableList<String> = ArrayList()
    var isConnected = false;

    init {
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
                Log.d(TAG, "deliveryComplete: job done!")
            }

        })

        val options = MqttConnectOptions()
        try {
            mqttClient.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "Connection success")
                    isConnected = true
                    if (sub) subscribe("$topic")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Connection failure")
                    connectionFailure += 1
                    if (connectionFailure < 2) {
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
        isConnected = false
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
        if (topic == "$userId") {
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
                //   saveMatch(matchJSON)
            }
        }
    }


}