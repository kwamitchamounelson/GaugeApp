package com.example.gaugeapp.ui.exchange.registration.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.gaugeapp.ui.communityLoan.yourProfile.id.IdFragment
import com.example.gaugeapp.ui.exchange.registration.servicesExchage.ServicesExchangeFragment

class RegistrationViewPagerAdapter(val fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ServicesExchangeFragment()
            1 -> IdFragment()
            else -> ServicesExchangeFragment()
        }
    }
}