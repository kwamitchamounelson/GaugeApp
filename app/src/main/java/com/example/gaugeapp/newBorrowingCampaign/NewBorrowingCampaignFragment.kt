package com.example.gaugeapp.newBorrowingCampaign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaugeapp.R
import com.example.gaugeapp.items.ImageProfileItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.new_borrowing_campaign_fragment.*
import kotlinx.android.synthetic.main.new_borrowing_campaign_fragment.view.*

class NewBorrowingCampaignFragment : Fragment() {

    companion object {
        fun newInstance() = NewBorrowingCampaignFragment()
    }

    private lateinit var viewModel: NewBorrowingCampaignViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.new_borrowing_campaign_fragment, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(root.my_toolbar_new_borrowing_camp)
        setHasOptionsMenu(true)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setOnClickListner()
        updateUI()
    }

    private fun setOnClickListner() {
        id_borrowing_cam_amount.setOnClickListener { }

        id_borrowing_cam_interest.setOnClickListener { }

        id_borrowing_cam_payment_date.setOnClickListener { }

        id_borrowing_cam_add_reason.setOnClickListener { }

        id_borrowing_cam_add_guarantor.setOnClickListener { }
    }

    private fun updateUI() {

        val image =
            "https://firebasestorage.googleapis.com/v0/b/kola-wallet-dev.appspot.com/o/USER_PROFILS%2F7MdBbt53EIQeAyIpGXkJaaCTEjb2?alt=media&token=0d2ed20f-a261-4f0a-a17e-f0c19671ebfb"

        val items = (0..3).map {
            ImageProfileItem(image)
        }

        id_borrowing_cam_guarantor_rv.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = GroupAdapter<ViewHolder>().apply {
                add(Section(items))
            }
        }
    }

}