package com.example.sharemycar.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sharemycar.R
import com.example.sharemycar.data.models.Directions
import com.example.sharemycar.data.retrofit.service.rest.Route
import com.example.sharemycar.data.untracked.PLACES_KEY
import com.example.sharemycar.data.util.MapsController
import com.example.sharemycar.data.util.Singleton
import com.example.sharemycar.ui.viewmodels.MapsViewModels
import com.example.sharemycar.ui.viewmodels.ResearchViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_maps.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import splitties.toast.toast

class MapsFragment : Fragment() {

    private val mapsViewModels: MapsViewModels by viewModels()

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mMapsController: MapsController

    private val callback = OnMapReadyCallback { googleMap ->
        mGoogleMap = googleMap

        mMapsController = MapsController(requireContext(), mGoogleMap)


        val directionsCall = Singleton.googleService.getDirections(
            convertLatLng(LatLng(48.862725,2.287592)),
            convertLatLng(LatLng(48.862725,1.287597)),
            PLACES_KEY
        )
        directionsCall.enqueue(object : Callback<Directions> {
            override fun onResponse(
                call: Call<Directions>,
                response: Response<Directions>
            ) {
                val directions = response.body()!!

                if (directions.status.equals("OK")) {
                    val legs = directions.routes[0].legs[0]
                    val route = Route(
                        getString(R.string.time_square),
                        getString(R.string.chelsea_market),
                        legs.startLocation.lat,
                        legs.startLocation.lng,
                        legs.endLocation.lat,
                        legs.endLocation.lng,
                        directions.routes[0].overviewPolyline.points
                    )

                    mMapsController.setMarkersAndRoute(route)
                }

                progressLayout.visibility = View.GONE
            }

            override fun onFailure(call: Call<Directions>, t: Throwable) {
                toast(t.toString())
                progressLayout.visibility = View.GONE

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
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapsViewModels.sendPosition()
        mapFragment?.getMapAsync(callback)

    }
    fun convertLatLng(latLng: LatLng):String = "${latLng.latitude},${latLng.longitude}"
}

