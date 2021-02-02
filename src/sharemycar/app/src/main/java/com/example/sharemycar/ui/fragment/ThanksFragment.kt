package com.example.sharemycar.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.sharemycar.R
import com.example.sharemycar.data.retrofit.service.rest.RequesterTypeEnum
import com.example.sharemycar.data.mqtt.MqttCommunicator
import com.example.sharemycar.databinding.FragmentThanksBinding
import com.example.sharemycar.ui.viewmodels.MatchViewModel
import com.example.sharemycar.ui.viewmodels.SessionViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [ThanksFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ThanksFragment : Fragment() {

    private val matchViewModel: MatchViewModel by viewModels()
    private val sessionViewModel: SessionViewModel by activityViewModels()

    private var _binding: FragmentThanksBinding? = null
    private val binding get() = _binding!!
    private lateinit var mqtt: MqttCommunicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentThanksBinding.inflate(inflater, container, false)
        displayThanksMessage()
        binding.backHomeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_thanksFragment_to_homePage)
        }

        /*sessionViewModel.user.observe(viewLifecycleOwner, Observer {
            if(it != null)
                mqtt = MqttCommunicator(requireContext(), it.id, matchViewModel);
        })*/
        mqtt = MqttCommunicator(requireContext(), 666, matchViewModel);

        return binding.root
    }

    fun displayThanksMessage() {

            if(sessionViewModel.requesterTypeEnum == RequesterTypeEnum.DRIVER)
                changeThanksMessage("Merci d'avoir pris des passagers sur votre trajet.\nLa Nature te remercie")
            else
                changeThanksMessage("Votre partenaire de covoiturage est là.\nNous te souhaitons un bon voyage")

    }

    fun changeThanksMessage(str: String) {
        binding.thankingMsg.text = str
    }
}