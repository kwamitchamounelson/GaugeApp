package com.example.gaugeapp.campaignList.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.gaugeapp.campaignList.all.AllCampaignFragment
import com.example.gaugeapp.campaignList.contact.ContactCampaignFragment
import com.example.gaugeapp.campaignList.lend.LendCampaignFragment

class CampaignMainViewPagerAdapter(val fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllCampaignFragment()
            1 -> ContactCampaignFragment()
            2 -> LendCampaignFragment()
            else -> AllCampaignFragment()
        }
    }
}