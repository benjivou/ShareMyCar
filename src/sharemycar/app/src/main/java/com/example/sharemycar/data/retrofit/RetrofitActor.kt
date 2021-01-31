package com.example.sharemycar.data.retrofit

import androidx.lifecycle.MutableLiveData
import com.example.sharemycar.data.displayabledata.DataPreprared
import com.example.sharemycar.data.displayabledata.EmptyDataPreprared
import com.example.sharemycar.data.displayabledata.ErrorDataPreprared
import com.example.sharemycar.data.displayabledata.SuccessDataPreprared
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitActor<T>(val data: MutableLiveData<DataPreprared<T>?>) : Callback<T> {
    override fun onResponse(call: Call<T>, response: Response<T>) {
        val body = response.body()
        if (body != null) {
            data.value = SuccessDataPreprared(body)
        }
        data.value = EmptyDataPreprared()
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        data.value = t.message?.let { ErrorDataPreprared(0, it) }
    }
}