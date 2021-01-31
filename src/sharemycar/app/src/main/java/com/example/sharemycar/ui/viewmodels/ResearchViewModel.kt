package com.example.sharemycar.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sharemycar.data.displayabledata.DataPreprared
import com.example.sharemycar.data.models.User
import com.example.sharemycar.data.retrofit.RetrofitActor
import com.example.sharemycar.data.retrofit.service.rest.MatchObject
import com.example.sharemycar.data.retrofit.service.rest.PositionLatLong
import com.example.sharemycar.data.retrofit.service.rest.RequesterTypeEnum
import com.example.sharemycar.data.retrofit.service.rest.UserStartProcess
import com.example.sharemycar.data.util.Singleton

/**
 * Start the first step of the process of searching for infos
 */
class ResearchViewModel : ViewModel() {
    val data: MutableLiveData<DataPreprared<MatchObject>?> =
        MutableLiveData(null) // current user loaded

    fun startSearchProcess(requester: RequesterTypeEnum, user: User, dest: PositionLatLong,positionLatLong: PositionLatLong,maxDist:Int?) {
        val result = Singleton.sessionsService.startProcess(
            MatchObject(
                requester,
                UserStartProcess(user.id, user.username),
                dest,
                positionLatLong,
                maxDist
                )
        )
        result.enqueue(RetrofitActor(data))
    }
}