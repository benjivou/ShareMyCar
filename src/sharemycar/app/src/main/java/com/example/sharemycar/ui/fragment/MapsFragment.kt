package com.example.sharemycar.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.sharemycar.R
import com.example.sharemycar.data.displayabledata.EmptyDataPreprared
import com.example.sharemycar.data.displayabledata.ErrorDataPreprared
import com.example.sharemycar.data.displayabledata.SuccessDataPreprared
import com.example.sharemycar.data.util.MapsController
import com.example.sharemycar.ui.viewmodels.MapsViewModels
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import splitties.toast.toast

class MapsFragment : Fragment() {
    val args: MapsFragmentArgs by navArgs()
    private lateinit var mapsViewModels: MapsViewModels

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
            MapsViewModels(requireContext(), Gson().fromJson(args.dest, LatLng::class.java))

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        mapFragment?.getMapAsync(callback)

    }

}

