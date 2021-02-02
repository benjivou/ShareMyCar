package com.example.sharemycar.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sharemycar.data.displayabledata.DataPreprared
import com.example.sharemycar.data.displayabledata.ErrorDataPreprared
import com.example.sharemycar.data.models.User
import com.example.sharemycar.data.retrofit.RetrofitActor
import com.example.sharemycar.data.retrofit.MatchObject
import com.example.sharemycar.data.retrofit.PositionLatLong
import com.example.sharemycar.data.retrofit.RequesterTypeEnum
import com.example.sharemycar.data.retrofit.UserStartProcess
import com.example.sharemycar.data.util.Singleton
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

/**
 * Start the first step of the process of searching for infos
 */
private const val TAG = "ResearchViewModel"

class ResearchViewModel : ViewModel() {
    val data: MutableLiveData<DataPreprared<MatchObject>?> =
        MutableLiveData(null) // current user loaded

    private var latLong: PositionLatLong? = null

    val placeSelectionListener: PlaceSelectionListener = object : PlaceSelectionListener {
        override fun onPlaceSelected(place: Place) {
            place.latLng?.run { latLong = PositionLatLong(latitude, longitude) } ?: run {
                Log.e(
                    TAG,
                    "onPlaceSelected: Place selection : bad configuration missing PositionLatLong"
                )
            }
        }

        override fun onError(status: Status) {
            Log.i(TAG, "An error occurred: $status")
        }
    }

    fun startSearchProcess(
        requester: RequesterTypeEnum,
        user: User,
        maxDist: Int?
    ) {
        latLong?.let {
            val result = Singleton.sessionsService.startProcess(

                MatchObject(
                    requester,
                    UserStartProcess(user.id, user.username),
                    it,
                    maxDist
                )
            )
            result.enqueue(RetrofitActor(data))
        } ?: run {
            this.data.value = ErrorDataPreprared(
                0,
                "Probléme de communication avec le service Google veuiller réessayer ultérieurement"
            )
        }
    }


}