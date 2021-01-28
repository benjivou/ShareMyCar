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
import com.example.sharemycar.data.displayabledata.EmptyDataPreprared
import com.example.sharemycar.data.displayabledata.ErrorDataPreprared
import com.example.sharemycar.data.displayabledata.SuccessDataPreprared
import com.example.sharemycar.data.retrofit.service.RequestMessage
import com.example.sharemycar.databinding.FragmentRegistrationBinding
import com.example.sharemycar.ui.viewmodels.LoginViewModel
import com.example.sharemycar.ui.viewmodels.RegisterViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import splitties.toast.toast


/**
 * A simple [Fragment] subclass.
 * Use the [RegistrationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val TAG = "RegistrationFragment"

class RegistrationFragment : Fragment() {

    private val profilViewModel: RegisterViewModel by viewModels()

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profilViewModel.data.observe(viewLifecycleOwner, Observer { apiResponse ->
            when (apiResponse) {
                is EmptyDataPreprared -> {
                    toast("Welcome")
                    findNavController().popBackStack()
                }
                is ErrorDataPreprared -> toast(apiResponse.errorMessage)
                is SuccessDataPreprared -> {
                    TODO()
                }

            }
        })
        // when a response

        binding.apply {
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }

            registrationProcessBtn.setOnClickListener {
                 profilViewModel.register(
                    binding.loginInputTxt.text.toString(),
                    binding.passwordInputTxt.text.toString()
                )
            }
        }



    }


}


