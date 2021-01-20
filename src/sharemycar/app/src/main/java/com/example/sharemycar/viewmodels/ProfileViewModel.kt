package com.example.sharemycar.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sharemycar.data.models.User


class ProfileViewModel : ViewModel(){
    val user: MutableLiveData<User?> = MutableLiveData(null)
}