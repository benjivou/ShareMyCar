package com.example.sharemycar.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.sharemycar.R
import com.example.sharemycar.databinding.FragmentHomeBinding
import com.example.sharemycar.databinding.FragmentLoginBinding
import com.example.sharemycar.ui.adapter.COLLECTION_HOME_DRIVER
import com.example.sharemycar.ui.adapter.COLLECTION_HOME_MAIN
import com.example.sharemycar.ui.adapter.COLLECTION_HOME_PASSENGER
import com.example.sharemycar.ui.adapter.HomeCollectionAdapter
import com.example.sharemycar.ui.viewmodels.SessionViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * A simple [Fragment] subclass.
 * Use the [HomePageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomePageFragment : Fragment() {

    private val userViewModel: SessionViewModel by activityViewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeCollectionAdapter: HomeCollectionAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeCollectionAdapter = HomeCollectionAdapter(childFragmentManager,lifecycle)

        binding.apply {

            viewPager.adapter = homeCollectionAdapter


            // Name the tab
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = when(position){
                    COLLECTION_HOME_PASSENGER -> "Passager"
                    COLLECTION_HOME_DRIVER -> "Conducteur"
                    COLLECTION_HOME_MAIN -> "Accueil"
                    else -> "ERROR"
                }
            }.attach()
        }


        // ecoute de la connexion de l'utilisateur
        userViewModel.user.observe(viewLifecycleOwner, Observer { user ->
            if (user == null) {
                findNavController().navigate(R.id.loginFragment)
            }
        })
    }

}
