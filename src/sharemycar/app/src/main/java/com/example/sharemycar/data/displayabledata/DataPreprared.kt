package com.example.sharemycar.data.displayabledata

sealed class DataPreprared<T>()

class EmptyDataPreprared<T> : DataPreprared<T>()
data class ErrorDataPreprared<T>(val errorCode: Int, val errorMessage: String) : DataPreprared<T>()
data class SuccessDataPreprared<T>(var content: T) : DataPreprared<T>()