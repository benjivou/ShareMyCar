package com.example.sharemycar.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sharemycar.data.displayabledata.DataPreprared
import com.example.sharemycar.data.displayabledata.ErrorDataPreprared
import com.example.sharemycar.data.models.LoginBucket
import com.example.sharemycar.data.models.User
import com.example.sharemycar.data.models.UserAuthentificaticator
import com.example.sharemycar.data.retrofit.RetrofitActor
import com.example.sharemycar.data.util.Singleton


class LoginViewModel : ViewModel() {
    val data: MutableLiveData<DataPreprared<User>?> = MutableLiveData(null) // current user loaded

    // login process
    fun login(username: String, password: String) {
        val result = Singleton.userService.loginPost(LoginBucket(username, password))
        result.enqueue(RetrofitActor(data))
    }

    fun register(username: String, password: String, repassword: String, email: String) {
        if (repassword != password) {
            data.value = ErrorDataPreprared(0, "Mot de passe différents")
            return
        }
        val result = Singleton.userService.registerPost(
            UserAuthentificaticator(username, password, email)
        )
        result.enqueue(RetrofitActor(data))
    }
}






