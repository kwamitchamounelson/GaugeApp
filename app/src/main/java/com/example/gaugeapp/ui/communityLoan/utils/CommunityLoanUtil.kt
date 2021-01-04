package com.example.gaugeapp.ui.communityLoan.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import com.example.gaugeapp.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

object CommunityLoanUtil {

    const val POOR_COLOR = "#FF6A6A"
    const val AVERAGE_COLOR = "#C465FF"
    const val GOOD_COLOR = "#FEBF47"
    const val EXCELLENT_COLOR = "#05BA49"
    const val NOT_COLOR = "#B5B5B5"

    fun initPieChartByUserScore(userScore: Double, pieChart: PieChart) {
        val yvalues = arrayListOf<PieEntry>()
        yvalues.add(PieEntry(10f, 0))
        yvalues.add(PieEntry(10f, 1))
        yvalues.add(PieEntry(10f, 2))
        yvalues.add(PieEntry(10f, 3))
        yvalues.add(PieEntry(10f, 4))

        val dataSet = PieDataSet(yvalues, "")

        dataSet.sliceSpace = 2f
        dataSet.colors =
            mutableListOf(
                getColorByScoreOfRange(userScore, 20, Color.parseColor(POOR_COLOR)),
                getColorByScoreOfRange(userScore, 40, Color.parseColor(POOR_COLOR)),
                getColorByScoreOfRange(userScore, 60, Color.parseColor(AVERAGE_COLOR)),
                getColorByScoreOfRange(userScore, 80, Color.parseColor(GOOD_COLOR)),
                getColorByScoreOfRange(userScore, 100, Color.parseColor(EXCELLENT_COLOR))
            )

        val data = PieData(dataSet)
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.DKGRAY)
        data.setDrawValues(false)

        pieChart.data = data
        pieChart.isRotationEnabled = false
        pieChart.isHighlightPerTapEnabled = false
        pieChart.transparentCircleRadius = 0f
        pieChart.isDrawHoleEnabled = true

        pieChart.maxAngle = 180.0f
        pieChart.rotationAngle = 180.0f
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        pieChart.holeRadius = 80f
        pieChart.centerText = userScore.toInt().toString()
        pieChart.setCenterTextSize(30f)
        pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD)

        pieChart.setCenterTextOffset(0f, -45f)

        pieChart.legend.isEnabled = false
        pieChart.setExtraOffsets(0f, 0f, 0f, -120f)
    }

    fun getAppreciation(userScore: Double, context: Context): String {
        return when {
            (userScore in 0.0..40.0) -> {
                context.getString(R.string.poor)
            }
            (userScore > 40 && userScore <= 60) -> {
                context.getString(R.string.average)
            }
            (userScore > 60 && userScore <= 80) -> {
                context.getString(R.string.good)
            }
            else -> {
                context.getString(R.string.excellent)
            }
        }
    }

    fun getColorOfScore(userScore: Double): Int {
        return when {
            (userScore in 0.0..40.0) -> {
                Color.parseColor(POOR_COLOR)
            }
            (userScore > 40 && userScore <= 60) -> {
                Color.parseColor(AVERAGE_COLOR)
            }
            (userScore > 60 && userScore <= 80) -> {
                Color.parseColor(GOOD_COLOR)
            }
            else -> {
                Color.parseColor(EXCELLENT_COLOR)
            }
        }
    }

    private fun getColorByScoreOfRange(
        userScore: Double,
        maxScore: Int,
        color: Int
    ): Int? {

        val tempScore = if (userScore != 0.0) {
            userScore
        } else {
            1.0
        }
        return if (tempScore >= maxScore || (maxScore - tempScore) < 20) {
            color
        } else {
            Color.parseColor(NOT_COLOR)
        }
    }
}