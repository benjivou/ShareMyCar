package com.example.sharemycar.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.sharemycar.R
import com.example.sharemycar.databinding.FragmentHomeBinding
import com.example.sharemycar.databinding.FragmentMainHomeBinding
import com.example.sharemycar.ui.viewmodels.SessionViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_home.*

private const val TAG = "MainHomeFragment"
class MainHomeFragment : Fragment() {
    private val userViewModel: SessionViewModel by activityViewModels()

    private var _binding: FragmentMainHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            userTitleTxt.text = "Bonjour, ${userViewModel.user.value!!.username}"
        }
    }
}