package com.example.gaugeapp.ui.communityLoan.yourProfile.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.gaugeapp.ui.communityLoan.yourProfile.aboutYou.AboutYouFragment
import com.example.gaugeapp.ui.communityLoan.yourProfile.id.IdFragment

class YourProfileViewPagerAdapter(val fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AboutYouFragment()
            1 -> IdFragment()
            else -> AboutYouFragment()
        }
    }
}