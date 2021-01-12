package com.example.gaugeapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Balance(
    @PrimaryKey
    var Id: Long,
    var operator: String,
    var balance: Double
) {
    constructor() : this(0, "", 0.0)
}