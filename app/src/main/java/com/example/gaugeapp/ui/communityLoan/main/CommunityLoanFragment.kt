package com.example.gaugeapp.ui.communityLoan.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.gaugeapp.R
import com.example.gaugeapp.ui.communityLoan.items.CampaignItem
import com.example.gaugeapp.ui.communityLoan.utils.CommunityLoanUtil
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.community_loan_fragment.*

class CommunityLoanFragment : Fragment() {

    companion object {
        fun newInstance() = CommunityLoanFragment()
    }

    private lateinit var viewModel: CommunityLoanViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.community_loan_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CommunityLoanViewModel::class.java)
        updateUI()

        setOnclickListner()
    }

    private fun setOnclickListner() {
        id_user_profile_block.setOnClickListener {
            updateUI()
        }

        id_com_loan_deb_to_pay_back.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_campaign_home_to_debToPayBackFragment)
        }

        id_com_loan_deb_receive.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_campaign_home_to_debToReceiveFragment)
        }

        id_com_loan_see_all_campaign.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_campaign_home_to_campaignLislFragment)
        }

        id_credit_score.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_campaign_home_to_creditScoreFragment)
        }

        id_user_profile_block.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_campaign_home_to_yourProfileFragment)
        }
    }

    private fun updateUI() {

        Glide.with(this)
            .load("https://firebasestorage.googleapis.com/v0/b/kola-wallet-dev.appspot.com/o/USER_PROFILS%2F7MdBbt53EIQeAyIpGXkJaaCTEjb2?alt=media&token=0d2ed20f-a261-4f0a-a17e-f0c19671ebfb")
            .error(R.drawable.ic_person_black_24dp)
            .circleCrop()
            .into(id_com_loan_user_profile)

        val userScore = (0..100).random()

        initScore(userScore.toDouble())


        updateRv()
    }

    private fun initScore(userScore: Double) {
        CommunityLoanUtil.initPieChartByUserScore(userScore, pieChart)
        id_com_loan_score_appreciation.text =
            CommunityLoanUtil.getAppreciation(userScore, requireContext())
    }

    private fun updateRv() {
        val items = (0..10).map {
            CampaignItem()
        }

        id_campaigns_rv.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = GroupAdapter<ViewHolder>().apply {
                add(Section(items))
            }
        }
    }

}