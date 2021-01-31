package com.example.sharemycar.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sharemycar.ui.fragment.DriverHomeFragment
import com.example.sharemycar.ui.fragment.LoginFragment
import com.example.sharemycar.ui.fragment.MainHomeFragment
import com.example.sharemycar.ui.fragment.PassengerHomFragment

const val COLLECTION_HOME_MAIN = 1
const val COLLECTION_HOME_DRIVER = 0
const val COLLECTION_HOME_PASSENGER = 2
class HomeCollectionAdapter(fm: FragmentManager,lifecycle: Lifecycle) : FragmentStateAdapter(fm,lifecycle){

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position){
            COLLECTION_HOME_MAIN -> MainHomeFragment()
            COLLECTION_HOME_DRIVER -> DriverHomeFragment()
            COLLECTION_HOME_PASSENGER -> PassengerHomFragment()
            else -> TODO("not Implemented")
        }
    }
}