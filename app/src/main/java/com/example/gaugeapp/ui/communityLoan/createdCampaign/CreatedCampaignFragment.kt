package com.example.gaugeapp.ui.communityLoan.createdCampaign

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gaugeapp.R
import com.example.gaugeapp.ui.communityLoan.items.CampaignItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.created_campaign_fragment.*
import kotlinx.android.synthetic.main.created_campaign_fragment.view.*

class CreatedCampaignFragment : Fragment() {

    companion object {
        fun newInstance() = CreatedCampaignFragment()
    }

    private lateinit var viewModel: CreatedCampaignViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.created_campaign_fragment, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(root.my_toolbar_created_campaign)
        setHasOptionsMenu(true)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateUi()
    }

    private fun updateUi() {
        val items = (0..10).map {
            CampaignItem()
        }

        id_created_campaign_list_main_rv.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = GroupAdapter<ViewHolder>().apply {
                add(Section(items))
            }
        }
    }

}