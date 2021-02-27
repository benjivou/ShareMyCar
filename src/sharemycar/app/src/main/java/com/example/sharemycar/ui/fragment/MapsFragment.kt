package com.example.sharemycar.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.sharemycar.R
import com.example.sharemycar.data.displayabledata.EmptyDataPreprared
import com.example.sharemycar.data.displayabledata.ErrorDataPreprared
import com.example.sharemycar.data.displayabledata.SuccessDataPreprared
import com.example.sharemycar.data.models.BucketMatch
import com.example.sharemycar.data.mqtt.ErrorMQTTPreprared
import com.example.sharemycar.data.mqtt.SuccessMQTTPreprared
import com.example.sharemycar.data.util.MapsController
import com.example.sharemycar.ui.viewmodels.MapsViewModels
import com.example.sharemycar.ui.viewmodels.MatchViewModel
import com.example.sharemycar.ui.viewmodels.SessionViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import splitties.toast.toast
import java.lang.Exception

private const val TAG = "MapsFragment"

class MapsFragment : Fragment() {
    val args: MapsFragmentArgs by navArgs()
    private lateinit var mapsViewModels: MapsViewModels
    private lateinit var matchViewModels: MatchViewModel

    private val userViewModel: SessionViewModel by activityViewModels()

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mMapsController: MapsController

    private val callback = OnMapReadyCallback { googleMap ->
        mGoogleMap = googleMap

        mMapsController = MapsController(requireContext(), mGoogleMap)
        mapsViewModels.pathToTarget.observe(viewLifecycleOwner, Observer {
            when (it) {
                is EmptyDataPreprared -> toast("Empty message")
                is ErrorDataPreprared -> toast(it.errorMessage)
                is SuccessDataPreprared -> {
                    mMapsController.clearMarkersAndRoute()
                    mMapsController.setMarkersAndRoute(it.content)
                }
                null -> TODO()
            }
        })
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapsViewModels =
            MapsViewModels(
                requireContext(),
                Gson().fromJson(args.dest, LatLng::class.java),
                userViewModel.user.value!!.id
            )
        matchViewModels = MatchViewModel(
            requireContext(),
            userViewModel.user.value!!,
        )

        // POP de like
        matchViewModels.mqttIdTopicLiveData.observe(viewLifecycleOwner, Observer {
            try {
                when (it) {
                    is ErrorMQTTPreprared -> MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.titleErrorMQTT))
                        .setMessage(it.errorMessage)
                        .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                            dialog.dismiss()
                        }
                        .show()
                    is SuccessMQTTPreprared -> {
                        val bucketMatch= Gson().fromJson(it.content, BucketMatch::class.java)
                        bucketMatch.driver?.run {
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle(resources.getString(R.string.titleMatchReady))
                                .setMessage(this.username + " veut te prendre es tu prêts? ")
                                .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                                    matchViewModels.sendAnswer(true,bucketMatch )
                                    dialog.dismiss()
                                }
                                .setNegativeButton(resources.getString(R.string.dismiss)) { dialog, which ->
                                    matchViewModels.sendAnswer(true,bucketMatch )
                                    Log.d(TAG, "onViewCreated: Passenger accepted")
                                    dialog.dismiss()
                                }
                                .show()
                        } ?: kotlin.run {
                            Log.i(TAG, "onViewCreated: no Driver") }
                        bucketMatch.passenger?.run {
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle(resources.getString(R.string.titleMatchReadyPassengerAvailable))
                                .setMessage(this.username + " veut te prendre es tu prêts? ")
                                .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                                    matchViewModels.sendAnswer(true,bucketMatch )
                                    Log.d(TAG, "onViewCreated: Passenger accepted")
                                    dialog.dismiss()
                                }
                                .setNegativeButton(resources.getString(R.string.dismiss)) { dialog, which ->
                                    matchViewModels.sendAnswer(true,bucketMatch )
                                    Log.d(TAG, "onViewCreated: Passenger accepted")
                                    dialog.dismiss()
                                }
                                .show()
                        }


                    }
                }
            }
            catch(e:Exception){
                kotlin.io.print(e)
            }
        })
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        mapFragment?.getMapAsync(callback)

    }

    override fun onDestroy() {
        super.onDestroy()
    }

}

