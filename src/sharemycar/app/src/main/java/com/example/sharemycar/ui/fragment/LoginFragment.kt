package com.example.sharemycar.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.sharemycar.R
import com.example.sharemycar.data.displayabledata.EmptyDisplayable
import com.example.sharemycar.data.displayabledata.ErrorDisplayable
import com.example.sharemycar.data.displayabledata.SuccessDisplayable
import com.example.sharemycar.databinding.FragmentLoginBinding
import com.example.sharemycar.ui.viewmodels.ProfileViewModel
import splitties.toast.toast

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val TAG = "LoginFragment"

class LoginFragment : Fragment() {

    private val profilViewModel: ProfileViewModel by activityViewModels()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // sub to the user infos
        profilViewModel.user.observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                findNavController().popBackStack()
            }
        })
        profilViewModel.userDisplayable.observe(viewLifecycleOwner, {
            when (it) {
                is EmptyDisplayable -> toast("Notre serveur à un probléme veuuiller réassayer")
                is ErrorDisplayable -> {
                    Log.d(TAG, "onViewCreated: $it")
                    toast("${it.errorMessage}")
                }
                is SuccessDisplayable -> toast(it.content)
            }
        })
        binding.apply {
            connexionBtn.setOnClickListener {
                profilViewModel.login(
                    loginInputTxt.text.toString(),
                    passwordInputTxt.text.toString()
                )
            }
            registrationBtn.setOnClickListener {
                findNavController().navigate(R.id.registrationFragment)
            }
        }

    }


}