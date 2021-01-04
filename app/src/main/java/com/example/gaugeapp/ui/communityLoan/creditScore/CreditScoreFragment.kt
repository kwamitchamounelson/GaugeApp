package com.example.gaugeapp.ui.communityLoan.creditScore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaugeapp.R
import com.example.gaugeapp.entities.ENUM_SCORE_CRITERIA
import com.example.gaugeapp.ui.communityLoan.items.ScoreCriteriaItem
import com.example.gaugeapp.ui.communityLoan.utils.CommunityLoanUtil
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.credit_score_fragment.*

class CreditScoreFragment : Fragment() {

    companion object {
        fun newInstance() = CreditScoreFragment()
    }

    private lateinit var viewModel: CreditScoreViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.credit_score_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val userScore = (0..100).random()

        initScore(userScore.toDouble())

        updateRv()
    }

    private fun initScore(userScore: Double) {
        CommunityLoanUtil.initPieChartByUserScore(userScore, pieChart_credit_score)
        id_credit_score_appreciation.text =
            CommunityLoanUtil.getAppreciation(userScore, requireContext())
    }

    private fun updateRv() {

        val items = ENUM_SCORE_CRITERIA.values().map {
            val userScore = (0..100).random()
            ScoreCriteriaItem(it, userScore.toDouble())
        }

        id_credit_score_level_list_rv.apply {
            layoutManager =
                LinearLayoutManager(requireContext())
            adapter = GroupAdapter<ViewHolder>().apply {
                add(Section(items))
            }
        }
    }

}