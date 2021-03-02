package com.example.sharemycar.ui.fragment

import android.os.Bundle
import android.util.Log
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
import com.example.sharemycar.databinding.FragmentLoginBinding
import com.example.sharemycar.ui.viewmodels.LoginViewModel
import com.example.sharemycar.ui.viewmodels.SessionViewModel
import splitties.toast.toast

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val TAG = "LoginFragment"

class LoginFragment : Fragment() {

    private val sessionViewModel: SessionViewModel by activityViewModels()
    private val loginViewModel: LoginViewModel by viewModels()

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
        sessionViewModel.user.observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                findNavController().popBackStack()
            }
        })

        binding.apply {

            loginViewModel.data.observe(viewLifecycleOwner, Observer { apiResponse ->
                connexionBtn.isEnabled = true
                when (apiResponse) {
                    is EmptyDataPreprared -> Log.w(TAG, "onViewCreated: void retrofit", )
                    is ErrorDataPreprared -> Log.w(
                        TAG,
                        "onViewCreated: ${apiResponse.errorMessage}"
                    )
                    is SuccessDataPreprared -> {
                        sessionViewModel.user.value = apiResponse.content
                        toast("you are logged in ")
                    }

                }
            })
            connexionBtn.setOnClickListener {
                connexionBtn.isEnabled = false
                loginViewModel.login(
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