package com.example.gaugeapp.ui.communityLoan.campaignMain.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaugeapp.R
import com.example.gaugeapp.ui.communityLoan.items.CampaignItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.contact_campaign_fragment.*

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
        updateUI()

        setOnCLickListner()
    }

    private fun setOnCLickListner() {
        id_created_campaign_see_all.setOnClickListener {
            findNavController().navigate(R.id.action_campaignLislFragment_to_createdCampaignFragment)
        }
    }

    private fun updateUI() {

        val items = (0..10).map {
            CampaignItem()
        }

        id_created_campaign_list_rv.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = GroupAdapter<ViewHolder>().apply {
                add(Section(items))
            }
        }


        id_contribute_campaign_list_rv.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = GroupAdapter<ViewHolder>().apply {
                add(Section(items))
            }
        }
    }

}