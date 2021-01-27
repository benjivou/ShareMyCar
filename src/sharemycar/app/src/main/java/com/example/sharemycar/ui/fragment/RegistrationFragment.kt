package com.example.sharemycar.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.sharemycar.R
import com.example.sharemycar.data.displayabledata.EmptyDisplayable
import com.example.sharemycar.data.displayabledata.ErrorDisplayable
import com.example.sharemycar.data.displayabledata.SuccessDisplayable
import com.example.sharemycar.data.models.User
import com.example.sharemycar.databinding.FragmentRegistrationBinding
import com.example.sharemycar.ui.viewmodels.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import org.json.JSONObject
import splitties.toast.toast


/**
 * A simple [Fragment] subclass.
 * Use the [RegistrationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val TAG = "RegistrationFragment"
class RegistrationFragment : Fragment() {

    private val profilViewModel: ProfileViewModel by activityViewModels()

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

        // when a response
        profilViewModel.userDisplayable .observe(viewLifecycleOwner, Observer { user ->
            when(user){
                is EmptyDisplayable -> toast("réponse inattendue")
                is ErrorDisplayable -> toast(user.errorMessage)
                is SuccessDisplayable ->   findNavController().navigate(R.id.loginFragment)
            }
        })

        binding.apply {
            backBtn.setOnClickListener {
                findNavController().navigate(R.id.loginFragment)
            }
            registrationProcessBtn.setOnClickListener {
                Log.d(TAG, "onViewCreated: ")
                profilViewModel.register(binding.loginInputTxt.text.toString(),binding.passwordInputTxt.text.toString())
            }
        }

    }


}