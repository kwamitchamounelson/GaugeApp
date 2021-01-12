package com.example.gaugeapp.data.entities

import android.os.Parcelable
import com.example.gaugeapp.data.enums.ENUMCATEGORYEXPENSE
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Cashflow(
    var id: String,
    var category: ENUMCATEGORYEXPENSE?,
    var date: Date,
    var image: String,
    var note: String,
    var amount: Double,
    var uri: String = "",
    var future: Boolean = false,
    val createdDate: Date = Date(),
    var dateNotification: Date = Date(),
    var budjetized: String = "",
    var isFromTransaction: Boolean = false
) : Parcelable {

    init {
        if (category == null) {
            category = ENUMCATEGORYEXPENSE.UNKNOWN
        }
//        id = "${this.createdDate.time}${this.hashCode()}"
    }

    constructor() : this("", null, Date(), "", "", 0.0)

    override fun equals(other: Any?): Boolean {
        if (other is Cashflow) {
            return other.createdDate.equals(this.createdDate)
        } else {
            return false
        }
    }
}