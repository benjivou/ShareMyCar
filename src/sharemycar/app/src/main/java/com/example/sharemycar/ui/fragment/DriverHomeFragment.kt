package com.example.sharemycar.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.sharemycar.databinding.FragmentDriverHomeBinding
import com.example.sharemycar.databinding.FragmentPassengerHomBinding
import com.example.sharemycar.ui.viewmodels.SessionViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DriverHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DriverHomeFragment : Fragment() {
    private val userViewModel: SessionViewModel by activityViewModels()

    private var _binding: FragmentDriverHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDriverHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
}