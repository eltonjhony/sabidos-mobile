package com.sabidos.infrastructure.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

fun today(): Calendar = GregorianCalendar.getInstance().apply {
    time = Date()
}

fun getWeeklyRankingTimerFormatted(endDate: Calendar): String {

    val days = endDate.difference(today()).days()
    val hourOfDay = endDate.get(Calendar.HOUR_OF_DAY) - today().get(Calendar.HOUR_OF_DAY)

    return if (days == 7L || hourOfDay == 0) {
        "${abs(days)}d"
    } else {
        "${abs(days)}d ${abs(hourOfDay)}h"
    }
}

const val hoursInWeek = 168f
fun getWeeklyRankingProgressFormatted(endDate: Calendar): Long =
    hoursInWeek.toLong() - endDate.difference(today()).hours()

fun Calendar.getNext(dayOfWeek: Int): Calendar {
    if (get(Calendar.DAY_OF_WEEK) == dayOfWeek) {
        add(Calendar.DATE, 7)
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
    } else {
        while (get(Calendar.DAY_OF_WEEK) != dayOfWeek) {
            add(Calendar.DATE, 1)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }
    }
    return this
}

fun Calendar.getLast(dayOfWeek: Int): Calendar {
    if (get(Calendar.DAY_OF_WEEK) == dayOfWeek) {
        add(Calendar.DATE, -7)
    } else {
        while (get(Calendar.DAY_OF_WEEK) != dayOfWeek) {
            add(Calendar.DATE, -1)
        }
    }
    return this
}

fun Calendar.format(pattern: String = "dd/MM/yyyy", locale: Locale = Locale.getDefault()): String {
    val format = SimpleDateFormat(pattern, locale)
    return format.format(this.time)
}

fun Date.format(pattern: String = "dd/MM/yyyy", locale: Locale = Locale.getDefault()): String {
    val format = SimpleDateFormat(pattern, locale)
    return format.format(this.time)
}

fun String.toDate(pattern: String = "dd/MM/yyyy", locale: Locale = Locale.getDefault()): Date? {
    val format = SimpleDateFormat(pattern, locale)
    return format.parse(this)
}

fun Calendar.difference(startDate: Calendar): Long = timeInMillis - startDate.timeInMillis
fun Long.days(): Long = (((this / 1000) / 60) / 60) / 24
fun Long.hours(): Long = (((this / 1000) / 60) / 60)