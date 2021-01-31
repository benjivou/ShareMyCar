package com.example.sharemycar.ui.viewmodels

import android.os.Handler
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MapsViewModels : ViewModel() {
    private val mInterval = 5000L // 5 seconds by default, can be changed later

    fun sendPosition(){
        viewModelScope.launch { // launch a new coroutine in background and continue
            while(true) {
                delay(mInterval) // non-blocking delay for 1 second (default time unit is ms)
                println("World!") // print after delay
            }
        }
    }
}