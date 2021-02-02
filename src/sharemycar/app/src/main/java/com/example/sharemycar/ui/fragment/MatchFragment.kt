package com.example.sharemycar.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.sharemycar.data.retrofit.RequesterTypeEnum
import com.example.sharemycar.databinding.MatchFragmentBinding
import com.example.sharemycar.ui.viewmodels.MatchViewModel
import com.example.sharemycar.ui.viewmodels.SessionViewModel

class MatchFragment : Fragment() {

    private var _binding : MatchFragmentBinding? = null
    private val binding get() = _binding!!;

    private val viewModel: MatchViewModel by viewModels()
    private val sessionViewModel: SessionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MatchFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.acceptBtn.setOnClickListener {

        }
        binding.refuseBtn.setOnClickListener {

        }
    }

    fun receivedNewMatch() {
        if(sessionViewModel.requesterTypeEnum === RequesterTypeEnum.PASSENGER) {
            viewModel.driver.observe(viewLifecycleOwner, Observer { user ->
                binding.userName.text = user.username
            })
        } else {
            viewModel.passenger.observe(viewLifecycleOwner, Observer { user ->
                binding.userName.text = user.username
            })
        }
    }

}