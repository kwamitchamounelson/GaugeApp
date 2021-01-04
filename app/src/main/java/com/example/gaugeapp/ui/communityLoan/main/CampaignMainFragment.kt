package com.example.gaugeapp.ui.communityLoan.main

import android.graphics.Color
import android.graphics.Typeface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.gaugeapp.R
import com.example.gaugeapp.ui.communityLoan.items.CampaignItem
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.campaign_main_fragment.*

class CampaignMainFragment : Fragment() {

    companion object {
        fun newInstance() = CampaignMainFragment()
    }

    private lateinit var viewModel: CampaignMainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.campaign_main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CampaignMainViewModel::class.java)
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
        val yvalues = arrayListOf<PieEntry>()
        yvalues.add(PieEntry(10f, 0))
        yvalues.add(PieEntry(10f, 1))
        yvalues.add(PieEntry(10f, 2))
        yvalues.add(PieEntry(10f, 3))
        yvalues.add(PieEntry(10f, 4))

        val dataSet = PieDataSet(yvalues, "")

        dataSet.sliceSpace = 2f
        dataSet.colors =
            mutableListOf(
                getColorByScoreOfRange(userScore, 20, Color.parseColor("#FF6A6A")),
                getColorByScoreOfRange(userScore, 40, Color.parseColor("#FF6A6A")),
                getColorByScoreOfRange(userScore, 60, Color.parseColor("#C465FF")),
                getColorByScoreOfRange(userScore, 80, Color.parseColor("#FEBF47")),
                getColorByScoreOfRange(userScore, 100, Color.parseColor("#05BA49"))
            )

        val data = PieData(dataSet)
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.DKGRAY)
        data.setDrawValues(false)

        pieChart.data = data
        pieChart.isRotationEnabled = false
        pieChart.isHighlightPerTapEnabled = false
        pieChart.transparentCircleRadius = 0f
        pieChart.isDrawHoleEnabled = true

        pieChart.maxAngle = 180.0f
        pieChart.rotationAngle = 180.0f
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        pieChart.holeRadius = 80f
        pieChart.centerText = userScore.toInt().toString()
        pieChart.setCenterTextSize(30f)
        pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD)

        pieChart.setCenterTextOffset(0f, -45f)

        pieChart.legend.isEnabled = false
        pieChart.setExtraOffsets(0f, 0f, 0f, -120f)

        id_com_loan_score_appreciation.text = getAppreciation(userScore)
    }

    private fun getAppreciation(userScore: Double): String {
        //TODO
        return "Appreciation"
    }

    private fun getColorByScoreOfRange(
        userScore: Double,
        maxScore: Int,
        color: Int
    ): Int? {

        val tempScore = if (userScore != 0.0) {
            userScore
        } else {
            1.0
        }
        return if (tempScore >= maxScore || (maxScore - tempScore) < 20) {
            color
        } else {
            Color.parseColor("#B5B5B5")
        }
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