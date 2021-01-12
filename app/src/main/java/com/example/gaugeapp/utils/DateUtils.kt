package com.example.gaugeapp.utils

import com.example.gaugeapp.data.entities.Period
import com.example.gaugeapp.utils.extentions.addDtates
import java.util.*

fun Date.toLong(): Long{
    return this.time
}

fun Long.fromLong(): Date{
    return Date(this)
}

fun Calendar.getMaximumOfMonth() : Calendar {
    this.set(Calendar.DAY_OF_MONTH, this.getMaximum(Calendar.DAY_OF_MONTH))
    this.set(Calendar.HOUR_OF_DAY, this.getMaximum(Calendar.HOUR_OF_DAY))
    this.set(Calendar.MINUTE, this.getMaximum(Calendar.MINUTE))
    return this
}

fun Calendar.getMinimumOfMonth() : Calendar {
    this.set(Calendar.DAY_OF_MONTH, this.getMinimum(Calendar.DAY_OF_MONTH))
    this.set(Calendar.HOUR_OF_DAY, this.getMinimum(Calendar.HOUR_OF_DAY))
    this.set(Calendar.MINUTE, this.getMinimum(Calendar.MINUTE))
    return this
}

fun Date.isBetween(debut: Date, fin: Date): Boolean{
    return this.after(debut) && this.before(fin)
}

fun isNowBetweenTwoDates (debut: Date, fin: Date) : Boolean {
    val now = Calendar.getInstance().time
    return now.isBetween(debut, fin)
}

fun numberOfDaysBetweenTwoDates(startDate: Date, endDate: Date) : Int {
    val diff = endDate.time - startDate.time
    val days = dateGetDays(diff)
    return days + 1
}

private fun dateGetDays(diff: Long): Int {
    return diff.div(1000*60*60*24).toInt()
}

fun getWeekOfAMonth (year: Int, month: Int) : List<Period>{
    val list = mutableListOf<Period>()

    val cal = Calendar.getInstance()
    (1..4).forEach {
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, it)
        cal.set(Calendar.MONTH, month)
        cal.set(Calendar.YEAR, year)

        val calfin = Calendar.getInstance()
        calfin.time =  cal.time.addDtates(6)
        calfin.set(Calendar.HOUR_OF_DAY, calfin.getMaximum(Calendar.HOUR_OF_DAY))

        cal.set(Calendar.HOUR_OF_DAY, cal.getMinimum(Calendar.HOUR_OF_DAY))

        list.add(Period(cal.time, calfin.time))
    }

    if (list.last().endDate.before(Calendar.getInstance().getMaximumOfMonth().time)){
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, 5)
        cal.set(Calendar.MONTH, month)
        cal.set(Calendar.YEAR, year)

        val calfin = Calendar.getInstance()
        calfin.time =  cal.time.addDtates(6)
        calfin.set(Calendar.HOUR_OF_DAY, calfin.getMaximum(Calendar.HOUR_OF_DAY))

        cal.set(Calendar.HOUR_OF_DAY, cal.getMinimum(Calendar.HOUR_OF_DAY))

        list.add(Period(cal.time, calfin.time))
    }

    return list
}