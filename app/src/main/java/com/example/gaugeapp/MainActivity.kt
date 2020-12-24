package com.example.gaugeapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gaugeapp.campaignDetail.CampaignDetailFragment
import com.example.gaugeapp.campaignHistory.CampaignHistoryFragment
import com.example.gaugeapp.campaignList.CampaignLislFragment
import com.example.gaugeapp.createdCampaign.CreatedCampaignFragment
import com.example.gaugeapp.guaranteeing.GuaranteeingFragment
import com.example.gaugeapp.guarantors.GuarantorsFragment
import com.example.gaugeapp.main.CampaignMainFragment
import com.example.gaugeapp.newBorrowingCampaign.NewBorrowingCampaignFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    GuarantorsFragment.newInstance()
                )
                .commitNow()
        }
    }
}