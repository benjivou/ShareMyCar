package com.example.sharemycar.data.displayabledata
// This class convert a response from the VM to displayable items
sealed class Displayable<T>()

class EmptyDisplayable<T> : Displayable<T>()
data class ErrorDisplayable<T>(val errorCode: Int, val errorMessage: String) : Displayable<T>()
data class SuccessDisplayable<T>(var content: T) : Displayable<T>()