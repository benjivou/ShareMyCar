package com.example.sharemycar.data.mqtt

sealed class BucketMQTT<T>()


data class ErrorMQTTPreprared<T>( val errorMessage: String) : BucketMQTT<T>()
data class SuccessMQTTPreprared<T>(var content: T) : BucketMQTT<T>()