package com.example.gaugeapp.ui.communityLoan.campaignMain.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.gaugeapp.ui.communityLoan.campaignMain.all.AllCampaignFragment
import com.example.gaugeapp.ui.communityLoan.campaignMain.contact.ContactCampaignFragment
import com.example.gaugeapp.ui.communityLoan.campaignMain.lend.LendCampaignFragment

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