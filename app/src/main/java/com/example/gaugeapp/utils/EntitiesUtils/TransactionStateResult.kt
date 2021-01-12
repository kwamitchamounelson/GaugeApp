package com.example.gaugeapp.utils.EntitiesUtils

class TransactionStateResult(val isGoodTransaction: Boolean, val reason: String) {
    constructor() : this(true, "")
}