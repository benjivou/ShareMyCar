package com.example.sharemycar.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.sharemycar.databinding.FragmentMainHomeBinding
import com.example.sharemycar.databinding.FragmentPassengerHomBinding
import com.example.sharemycar.ui.viewmodels.SessionViewModel

class PassengerHomFragment : Fragment() {
    private val userViewModel: SessionViewModel by activityViewModels()

    private var _binding: FragmentPassengerHomBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPassengerHomBinding.inflate(inflater, container, false)
        return binding.root
    }

}