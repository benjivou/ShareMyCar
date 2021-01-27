package com.example.sharemycar.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.sharemycar.R
import com.example.sharemycar.data.models.User
import com.example.sharemycar.databinding.FragmentRegistrationBinding
import kotlinx.android.synthetic.main.fragment_login.*
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 * Use the [RegistrationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val TAG = "RegistrationFragment"
class RegistrationFragment : Fragment() {


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

        binding.apply {
            backBtn.setOnClickListener {
                findNavController().navigate(R.id.loginFragment)
            }
            registrationProcessBtn.setOnClickListener {
                Log.d(TAG, "onViewCreated: ")
                registrationProcess()
            }
        }

    }

    fun registrationProcess(){
        // TODO
        AndroidNetworking
            .post("http://${getString(R.string.NODE_IP_ADDRESS)}:8080/api/users/new")
            .addBodyParameter("username", binding.loginInputTxt.text.toString())
            .addBodyParameter("password", binding.passwordInputTxt.text.toString())
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d(TAG, "onResponse: messsag recu ${response.toString()}")
                    findNavController().navigate(R.id.loginFragment)
                }

                override fun onError(anError: ANError?) {
                    Log.d(TAG, "onError: message")
                    Toast.makeText(
                        requireContext(),
                        anError.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e(TAG, "onError: ", anError)
                }

            })
        
    }

}