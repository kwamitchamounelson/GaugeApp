package com.example.gaugeapp.campaignList.contact

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gaugeapp.R

class ContactCampaignFragment : Fragment() {

    companion object {
        fun newInstance() = ContactCampaignFragment()
    }

    private lateinit var viewModel: ContactCampaignViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.contact_campaign_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ContactCampaignViewModel::class.java)
        // TODO: Use the ViewModel
    }

}