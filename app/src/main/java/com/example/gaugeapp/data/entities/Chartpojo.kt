package com.example.gaugeapp.data.entities

import com.kola.kola_entities_features.entities.DateForSort
import java.util.*

class Chartpojo(var amount:Double,var dateForSort: Date) {
    constructor():this(0.0,Date())
}