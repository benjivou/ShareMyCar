package com.example.sharemycar.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.sharemycar.viewmodels.ProfileViewModel
import com.example.sharemycar.R
import com.example.sharemycar.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * A simple [Fragment] subclass.
 * Use the [HomePageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomePageFragment : Fragment() {

    private val userViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentHomeBinding.inflate(inflater, container, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    /*    goScanQRbtn.setOnClickListener { findNavController().navigate(FragmentHomeBinding.actionHomePageToScanQr()) }
        goToFraudeBtn.setOnClickListener { findNavController().navigate(FragmentHomeBinding.actionHomePageToManuallFraudFormulaire()) }
        consultStatsBtn.setOnClickListener {
            findNavController().navigate(FragmentHomeBinding.actionHomePageToLoadingStats("5f99ac7584b0c83808bb1a95"))
        }*/

        // ecoute de la connexion de l'utilisateur
        userViewModel.user.observe(viewLifecycleOwner, Observer { user ->
            if (user == null) {
                findNavController().navigate(R.id.loginFragment)
            }
        })
    }

}
