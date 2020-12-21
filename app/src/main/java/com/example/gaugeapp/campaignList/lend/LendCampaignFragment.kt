package com.example.gaugeapp.campaignList.lend

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gaugeapp.R

class LendCampaignFragment : Fragment() {

    companion object {
        fun newInstance() = LendCampaignFragment()
    }

    private lateinit var viewModel: LendCampaignViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.lend_campaign_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LendCampaignViewModel::class.java)
        // TODO: Use the ViewModel
    }

}