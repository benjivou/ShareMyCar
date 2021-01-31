package com.example.sharemycar.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sharemycar.data.models.User

class SessionViewModel :ViewModel() {
    val user: MutableLiveData<User?> = MutableLiveData(null) // current user loaded
    val isDriver: MutableLiveData<Boolean> = MutableLiveData(false)
}