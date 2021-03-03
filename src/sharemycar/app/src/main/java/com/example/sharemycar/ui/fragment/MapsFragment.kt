package com.example.sharemycar.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import com.example.sharemycar.R
import com.example.sharemycar.data.displayabledata.EmptyDataPreprared
import com.example.sharemycar.data.displayabledata.ErrorDataPreprared
import com.example.sharemycar.data.displayabledata.SuccessDataPreprared
import com.example.sharemycar.data.models.Bucket1Match
import com.example.sharemycar.data.models.BucketMatch
import com.example.sharemycar.data.mqtt.ErrorMQTTPreprared
import com.example.sharemycar.data.mqtt.SuccessMQTTPreprared
import com.example.sharemycar.data.retrofit.RequesterTypeEnum
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
import kotlinx.android.synthetic.main.fragment_maps.*
import splitties.toast.toast

private const val TAG = "MapsFragment"

class MapsFragment : Fragment() {
    val args: MapsFragmentArgs by navArgs()
    private lateinit var mapsViewModels: MapsViewModels
    private lateinit var matchViewModels: MatchViewModel

    private val userViewModel: SessionViewModel by activityViewModels()

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mMapsController: MapsController

    private var currentMatch: BucketMatch? = null
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
        mapsViewModels.isThisTheEnd.observe(viewLifecycleOwner,{
            if(it){
                val directions = MapsFragmentDirections.actionMapsFragmentToThanksFragment()
                findNavController().navigate(directions)
            }
        })
        floatingActionButton2.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Avez-vous rejoint votre ${if(userViewModel.requesterTypeEnum == RequesterTypeEnum.PASSENGER) "chauffeur" else "passager"}?")
                .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->

                    dialog.dismiss()
                    findNavController().popBackStack()
                    toast("Merci d'avoir utilisé notre application ")
                }
                .setNegativeButton(resources.getString(R.string.dismiss)) { dialog, which ->
                    findNavController().popBackStack()
                    toast("Dommage une autre fois peut-être")
                }
                .show()
        }
        // POP de like
        matchViewModels.mqttIdTopicLiveData.observe(viewLifecycleOwner, Observer { bucketMQTT ->
            try {
                when (bucketMQTT) {
                    is ErrorMQTTPreprared -> MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.titleErrorMQTT))
                        .setMessage(bucketMQTT.errorMessage)
                        .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                            dialog.dismiss()
                        }
                        .show()
                    is SuccessMQTTPreprared -> {
                        var map: Map<String, String> = HashMap()
                        map = Gson().fromJson(bucketMQTT.content, map.javaClass)
                        // Check if it's a match or not
                        if (map.filterKeys { it == "name" }.isNotEmpty()) {
                            val bucketMatch =
                                Gson().fromJson(bucketMQTT.content, BucketMatch::class.java)
                            bucketMatch.run {
                                if (userViewModel.requesterTypeEnum == RequesterTypeEnum.PASSENGER)
                                    displayMatchPopup(this, userViewModel.requesterTypeEnum!!)
                                else
                                    displayMatchPopup(this, userViewModel.requesterTypeEnum!!)
                            }
                        } else if (bucketMQTT.content == "accept" || bucketMQTT.content == "refuse") {
                            displayMatchPopupResult(bucketMQTT.content == "accept")

                            Log.d(TAG, "onViewCreated: $bucketMQTT")
                        }
                        // this is the new topic
                        else if (map.filterKeys { it == "pub" }.isNotEmpty()){

                            mapsViewModels.changeListener(
                                Gson().fromJson(bucketMQTT.content, Bucket1Match::class.java)
                            )

                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "onViewCreated: $e", e)
            }
        })
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        mapFragment?.getMapAsync(callback)

    }

    fun displayMatchPopupResult(isAccepted: Boolean) {
        var msg: String =
            if (isAccepted) {
                resources.getString(R.string.msgMatchAccepted)
            } else {

                resources.getString(R.string.msgMatchRefused)
            }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.titleMatchResult))
            .setMessage(msg)
            .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->

                dialog.dismiss()
            }

            .show()
    }

    fun displayMatchPopup(bucketMatch: BucketMatch, requesterTypeEnum: RequesterTypeEnum) {
        var title: String
        var msg: String
        if (requesterTypeEnum == RequesterTypeEnum.DRIVER) {
            title = resources.getString(R.string.titleMatchReadyPassengerAvailable)
            msg = resources.getString(R.string.msgMatchReadyDriver, bucketMatch.name)
        } else {
            title = resources.getString(R.string.titleMatchReadyDriverAvailable)
            msg = resources.getString(R.string.msgMatchReadyPassenger, bucketMatch.name)
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(msg)
            .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                matchViewModels.sendAnswer(true, bucketMatch.id)
                dialog.dismiss()
            }
            .setNegativeButton(resources.getString(R.string.dismiss)) { dialog, which ->
                matchViewModels.sendAnswer(false, bucketMatch.id)
                Log.d(TAG, "onViewCreated: Passenger accepted")
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}

