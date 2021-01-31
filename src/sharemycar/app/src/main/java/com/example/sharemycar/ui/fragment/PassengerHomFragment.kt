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
import com.example.sharemycar.databinding.FragmentPassengerHomBinding
import com.example.sharemycar.ui.viewmodels.ResearchViewModel
import com.example.sharemycar.ui.viewmodels.SessionViewModel
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import splitties.toast.toast
import java.util.*


class PassengerHomFragment : Fragment() {


    private var _binding: FragmentPassengerHomBinding? = null
    private val binding get() = _binding!!


    private val sessionViewModel: SessionViewModel by activityViewModels()
    private val researchViewModel: ResearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPassengerHomBinding.inflate(inflater, container, false)
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

            startPassengerBtn.setOnClickListener {
                startPassengerBtn.isEnabled = false

                researchViewModel.startSearchProcess(
                    RequesterTypeEnum.DRIVER,
                    sessionViewModel.user.value!!,
                    null
                )

            }

            researchViewModel.data.observe(viewLifecycleOwner, Observer { apiResponse ->
                startPassengerBtn.isEnabled = true
                when (apiResponse) {
                    is EmptyDataPreprared -> toast("void answer")
                    is ErrorDataPreprared -> toast(apiResponse.errorMessage)
                    is SuccessDataPreprared -> {
                        sessionViewModel.requesterTypeEnum = RequesterTypeEnum.DRIVER
                        toast("We are searching for your partner ")
                        findNavController().navigate(HomePageFragmentDirections.actionHomePageToMapsFragment())
                    }

                }
            })
        }

    }

}
