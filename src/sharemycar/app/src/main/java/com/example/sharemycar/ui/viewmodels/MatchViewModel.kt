package com.example.sharemycar.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sharemycar.data.models.User

class MatchViewModel : ViewModel() {
    val driver: MutableLiveData<User> = MutableLiveData(null)
    val passenger: MutableLiveData<User> = MutableLiveData(null)
}