package com.example.gaugeapp.data.entities

data class SubBalanceData(var omBalance: Double, var momoBalance: Double, var filter: Int) {
    constructor() : this(0.0, 0.0, 0)
}