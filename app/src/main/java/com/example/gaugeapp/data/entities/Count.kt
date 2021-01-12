package com.example.gaugeapp.data.entities

data class Count(var functionId: String, var functionCalls: Int, var functiionCount: Int) {
    constructor() : this("", 0, 0)
}