package com.example.sharemycar.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sharemycar.data.displayabledata.DataPreprared
import com.example.sharemycar.data.displayabledata.EmptyDataPreprared
import com.example.sharemycar.data.displayabledata.ErrorDataPreprared
import com.example.sharemycar.data.displayabledata.SuccessDataPreprared
import com.example.sharemycar.data.models.User
import com.example.sharemycar.data.retrofit.service.LoginBucket
import com.example.sharemycar.data.retrofit.service.UserAuthentificaticator
import com.example.sharemycar.data.util.Singleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel : ViewModel() {
    val data: MutableLiveData<DataPreprared<User>?> = MutableLiveData(null) // current user loaded


    // login process
    fun login(username: String, password: String) {
        val result = Singleton.userService.loginPost(LoginBucket(username, password) )
        result.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                val body = response.body()
                if (body != null) {
                    data.value = SuccessDataPreprared(body)
                }
                data.value = EmptyDataPreprared()
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                data.value = t.message?.let { ErrorDataPreprared(0, it) }
            }
        })
    }
}


