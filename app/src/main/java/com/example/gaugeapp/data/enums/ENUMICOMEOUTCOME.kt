package com.example.gaugeapp.data.enums

import com.example.gaugeapp.R

enum class ENUMICOMEOUTCOME(val strNameId: Int, val iconeId: Int, val textColorId: Int) {
    INCOME(R.string.total_income, R.drawable.ic_arrow_down, R.color.color_i_come),
    OUTCOME(R.string.total_outcome, R.drawable.ic_arrow_up, R.color.color_out_come)
}