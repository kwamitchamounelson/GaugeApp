package com.example.gaugeapp.data.enums

import com.example.gaugeapp.R


enum class ENUM_SCORE_CRITERIA(
    val criteriaNameStringResource: Int,
    val criteriaDescriptionStringResource: Int
) {
    PAYBACKSPEED(R.string.payback_speed, R.string.payback_speed_description),
    TRANSACTIONS(R.string.transactions, R.string.transactions_description),
    GUARANTEEING(R.string.guaranteeing, R.string.guaranteeing_description),
    GUARANTORS(R.string.guarantors, R.string.guarantors_description),
}