package com.example.sharemycar.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.sharemycar.R
import com.example.sharemycar.data.displayabledata.EmptyDataPreprared
import com.example.sharemycar.data.displayabledata.ErrorDataPreprared
import com.example.sharemycar.data.displayabledata.SuccessDataPreprared
import com.example.sharemycar.data.retrofit.service.rest.RequesterTypeEnum
import com.example.sharemycar.databinding.FragmentDriverHomeBinding
import com.example.sharemycar.ui.viewmodels.ResearchViewModel
import com.example.sharemycar.ui.viewmodels.SessionViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.gson.Gson
import splitties.toast.toast
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
    private val sessionViewModel: SessionViewModel by activityViewModels()
    private val researchViewModel: ResearchViewModel by viewModels()

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
            Arrays.asList(
                Place.Field.NAME, Place.Field.LAT_LNG
            )
        );

        autocompleteFragment.setOnPlaceSelectedListener(researchViewModel.placeSelectionListener)


        binding.apply {

            startDriverBtn.setOnClickListener {
                startDriverBtn.isEnabled = false
                if (longDriverInput.text.toString().isNotBlank()) {
                    researchViewModel.startSearchProcess(
                        RequesterTypeEnum.DRIVER,
                        sessionViewModel.user.value!!,
                        Integer.parseInt(longDriverInput.text.toString())
                    )
                }
                else{
                    startDriverBtn.isEnabled = true
                    toast("veuillez saisir un détour maximum valide")
                }

            }
            researchViewModel.data.observe(viewLifecycleOwner, Observer { apiResponse ->
                startDriverBtn.isEnabled = true
                when (apiResponse) {
                    is ErrorDataPreprared -> toast(apiResponse.errorMessage)
                    is SuccessDataPreprared -> {
                        sessionViewModel.requesterTypeEnum = RequesterTypeEnum.DRIVER
                        toast("We are searching for your partner ")

                        val action = HomePageFragmentDirections.actionHomePageToMapsFragment(Gson().toJson(LatLng(apiResponse.content.dest.latitude,apiResponse.content.dest.longitude) ))
                        findNavController().navigate(action)
                    }

                }
            })
        }

    }
}