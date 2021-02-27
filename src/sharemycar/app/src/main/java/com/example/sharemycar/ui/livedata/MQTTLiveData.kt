package com.example.sharemycar.ui.livedata

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.sharemycar.data.mqtt.BucketMQTT
import com.example.sharemycar.data.mqtt.ErrorMQTTPreprared
import com.example.sharemycar.data.mqtt.SuccessMQTTPreprared
import com.example.sharemycar.data.mqtt.connectionFailure
import com.google.gson.GsonBuilder
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*


private const val TAG = "MQTTLiveData"

class MQTTLiveData(val ctx: Context, val topic: String, val userId: String) :
    LiveData<BucketMQTT<String>>() {

    private val SERVER_URL: String = "tcp://broker.emqx.io:1883"
    private lateinit var mqttClient: MqttAndroidClient


    override fun onInactive() {
        super.onInactive()
        disconnect()
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

    private fun unsubscribeToAll() {

        mqttClient.unsubscribe(topic.toString(), null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d(TAG, "Unsubscribed to $topic")
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.d(TAG, "Failed to unsubscribe $topic")
            }
        })

    }


    override fun onActive() {
        super.onActive()
        this.mqttClient = MqttAndroidClient(
            ctx,
            SERVER_URL,
            "ShareMyCarClient-${userId}"
        )
        connect(ctx)


    }

    fun subscribe(topic: String, qos: Int = 1) {
        Log.d(TAG, "TRYING TO SUBSCRIBE")

        try {
            mqttClient.subscribe(topic.toString(), qos, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "Subscribed to $topic")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Failed to subscribe $topic")
                }

            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun connect(context: Context) {
        mqttClient.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {
                Log.d(TAG, "Connection lost ${cause.toString()}")
                value = cause?.message?.let { ErrorMQTTPreprared(it) }
                    ?: ErrorMQTTPreprared("connection Lost")
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                Log.d(TAG, "Receive message: ${message.toString()} from topic: $topic")
                if (topic == topic) {
                    if (message.toString().trim() == "accept") {
                        // The match is accepted
                        Log.d(TAG, "THE MATCH IS ACCEPTED")
                    } else if (message.toString().trim() == "refuse") {
                        // The match is refuse
                        Log.d(TAG, "THE MATCH IS REFUSED")
                    } else {
                        val gson = GsonBuilder()

                        val buf: String = message.toString()

                        value = SuccessMQTTPreprared(
                            buf
                        )
                        Log.d(TAG, "Message received")
                    }
                }
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
                    subscribe(topic, 1)
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Connection failure")
                    connectionFailure += 1
                    if (connectionFailure < 2) {
                        Log.d(TAG, "Retrying to connect...")
                        mqttClient.connect()
                    }
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }


}
