package com.example.sharemycar.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.sharemycar.R
import com.example.sharemycar.databinding.FragmentDriverHomeBinding
import com.example.sharemycar.databinding.FragmentPassengerHomBinding
import com.example.sharemycar.ui.viewmodels.ResearchViewModel
import com.example.sharemycar.ui.viewmodels.SessionViewModel
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DriverHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val TAG = "DriverHomeFragment"
class DriverHomeFragment : Fragment() {
    private val userViewModel: SessionViewModel by activityViewModels()
    private val researchViewModel:ResearchViewModel by viewModels (  )

    private var _binding: FragmentDriverHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDriverHomeBinding.inflate(inflater, container, false)
        // Initialize the AutocompleteSupportFragment.

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment


        autocompleteFragment.setPlaceFields(
            Arrays.asList(Place.Field.ID,
                Place.Field.NAME,Place.Field.LAT_LNG));
        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                val latlng = place.latLng
            }

            override fun onError(status: Status) {
                Log.i(TAG, "An error occurred: $status")
            }
        })


    }
}