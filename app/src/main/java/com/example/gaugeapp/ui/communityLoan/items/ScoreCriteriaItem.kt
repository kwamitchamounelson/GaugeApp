package com.example.gaugeapp.ui.communityLoan.items

import com.example.gaugeapp.R
import com.example.gaugeapp.entities.ENUM_SCORE_CRITERIA
import com.example.gaugeapp.ui.communityLoan.utils.CommunityLoanUtil
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_credit_score_criteria.view.*
import org.jetbrains.anko.textColor

class ScoreCriteriaItem(val scoreCriteria: ENUM_SCORE_CRITERIA, val userScore: Double) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        val context = viewHolder.itemView.context
        viewHolder.itemView.id_score_criteria_name.text =
            context.getString(scoreCriteria.criteriaNameStringResource)

        viewHolder.itemView.id_score_criteria_description.text =
            context.getString(scoreCriteria.criteriaDescriptionStringResource)

        viewHolder.itemView.id_score_criteria_appreciation.apply {
            text =
                CommunityLoanUtil.getAppreciation(userScore, context)

            textColor = CommunityLoanUtil.getColorOfScore(userScore)
        }
    }

    override fun getLayout() = R.layout.item_credit_score_criteria
}