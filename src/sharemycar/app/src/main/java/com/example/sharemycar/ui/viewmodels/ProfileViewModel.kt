package com.example.sharemycar.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.sharemycar.data.displayabledata.Displayable
import com.example.sharemycar.data.displayabledata.EmptyDisplayable
import com.example.sharemycar.data.displayabledata.ErrorDisplayable
import com.example.sharemycar.data.displayabledata.SuccessDisplayable
import com.example.sharemycar.data.models.User
import com.example.sharemycar.data.retrofit.ApiEmptyResponse
import com.example.sharemycar.data.retrofit.ApiErrorResponse
import com.example.sharemycar.data.retrofit.ApiSuccessResponse
import com.example.sharemycar.data.retrofit.service.UserAuthentificaticator
import com.example.sharemycar.data.util.Singleton


class ProfileViewModel : ViewModel() {
    val user: MutableLiveData<User?> = MutableLiveData(null) // current user loaded
    var userDisplayable: MutableLiveData<Displayable<String>> = MutableLiveData(null)

    // login process
    fun login(username: String, password: String) {
        Transformations.map(
            Singleton.service.loginPost(
                UserAuthentificaticator(
                    username,
                    password
                )
            )
        ) {
            userDisplayable.value = when (it) {
                is ApiSuccessResponse -> {
                    user.value = it.body
                    SuccessDisplayable("message successfully received")
                }
                is ApiEmptyResponse -> EmptyDisplayable()
                is ApiErrorResponse -> ErrorDisplayable(
                    it.errorCode,
                    it.errorMessage
                )
            }
        }
    }

    fun register(username: String, password: String) {
        Transformations.map(
            Singleton.service.loginPost(
                UserAuthentificaticator(
                    username,
                    password
                )
            )
        ) {
            userDisplayable.value = when (it) {
                is ApiSuccessResponse -> SuccessDisplayable("user authentified")
                is ApiEmptyResponse -> EmptyDisplayable()
                is ApiErrorResponse -> ErrorDisplayable(
                    it.errorCode,
                    it.errorMessage
                )
            }
        }
    }
}