package com.yjk.drawdiaryv2.view.add.util

import java.util.*

class DateFactory {

    private var c: Calendar = Calendar.getInstance()
    var year = 0
    var month = 0
    var day = 0

    init {
        year = c.get(Calendar.YEAR) // current year
        month = c.get(Calendar.MONTH) // current month
        day = c.get(Calendar.DAY_OF_MONTH) // current day
    }

    fun setCalendar(year: Int, month: Int, day: Int) {
        c.set(year, month, day)
    }

    fun getDayOfWeek(): String {
        val nWeek = c[Calendar.DAY_OF_WEEK]
        var strWeek = "-"
        when (nWeek) {
            1 -> strWeek = "일"
            2 -> strWeek = "월"
            3 -> strWeek = "화"
            4 -> strWeek = "수"
            5 -> strWeek = "목"
            6 -> strWeek = "금"
            7 -> strWeek = "토"
        }
        return strWeek
    }

    fun getTime(): Long? {
        return System.currentTimeMillis()
    }
}