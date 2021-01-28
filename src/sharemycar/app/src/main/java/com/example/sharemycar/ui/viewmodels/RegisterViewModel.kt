package com.example.sharemycar.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sharemycar.data.displayabledata.DataPreprared
import com.example.sharemycar.data.displayabledata.EmptyDataPreprared
import com.example.sharemycar.data.displayabledata.ErrorDataPreprared
import com.example.sharemycar.data.displayabledata.SuccessDataPreprared
import com.example.sharemycar.data.retrofit.service.RequestMessage
import com.example.sharemycar.data.retrofit.service.UserAuthentificaticator
import com.example.sharemycar.data.util.Singleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel()  {
    val data: MutableLiveData<DataPreprared<RequestMessage>?> =
        MutableLiveData(null) // current user loaded

    fun register(username: String, password: String) {
        val result = Singleton.userService.registerPost(
            UserAuthentificaticator(username, password)
        )
        result.enqueue(object : Callback<RequestMessage> {
            override fun onResponse(
                call: Call<RequestMessage>,
                response: Response<RequestMessage>
            ) {
                val message = response.body()
                if (message != null) {
                    data.value = SuccessDataPreprared(message)
                }
                data.value = EmptyDataPreprared()
            }

            override fun onFailure(call: Call<RequestMessage>, t: Throwable) {
                data.value = t.message?.let { ErrorDataPreprared(0, it) }
            }
        })

    }
}