package com.example.gaugeapp.ui.communityLoan.campaignMain.all

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gaugeapp.R
import com.example.gaugeapp.ui.communityLoan.items.CampaignItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.all_campaign_fragment.*

class AllCampaignFragment : Fragment() {

    companion object {
        fun newInstance() = AllCampaignFragment()
    }

    private lateinit var viewModel: AllCampaignViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.all_campaign_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AllCampaignViewModel::class.java)
        updateUI()
    }

    private fun updateUI() {

        val items = (0..10).map {
            CampaignItem()
        }

        id_all_campaign_list_rv.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = GroupAdapter<ViewHolder>().apply {
                add(Section(items))
                setOnItemClickListener { item, view ->
                    findNavController().navigate(R.id.action_campaignLislFragment_to_campaignDetailFragment)
                }
            }
        }
    }

}