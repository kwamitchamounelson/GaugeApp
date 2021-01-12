package com.example.gaugeapp.data.entities

import android.os.Parcelable
import com.example.gaugeapp.utils.extentions.convertDateToSpecificStringFormat
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Period (val startDate: Date, val endDate: Date) :Parcelable{
    constructor() : this (Date(), Date())

    override fun toString(): String {
        return startDate.convertDateToSpecificStringFormat("dd MMM yy ") + " - " + endDate.convertDateToSpecificStringFormat("dd MMM yy")
    }
}