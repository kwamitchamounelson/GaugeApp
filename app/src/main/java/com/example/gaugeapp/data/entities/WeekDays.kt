package com.example.gaugeapp.data.entities

import com.kola.kola_entities_features.entities.DateForSort

data class WeekDays (var week:Int,var days:MutableList<DateForSort>){
    constructor():this(0, arrayListOf<DateForSort>())
}