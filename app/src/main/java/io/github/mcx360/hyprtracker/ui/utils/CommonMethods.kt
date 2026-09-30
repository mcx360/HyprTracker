package io.github.mcx360.hyprtracker.ui.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

//format date from internal database format YYYY-MM-DD to regular format DD/MM/YYYY
fun formatToRegularDate(date: String) : String{
    val dateInfo = date.split('-')
    return "${dateInfo[2]}/${dateInfo[1]}/${dateInfo[0]}"
}

//format date from database format YYYY-MM-DD to day-month-year formatting e.g. 18 May 2015 for nicer UI representation
fun formatToDayMonthYear(date: String) : String{
    val day = LocalDate.parse(date)
    return "${day.dayOfMonth} ${day.month.toString().substring(0,3).lowercase().replaceFirstChar {it.uppercase()}} ${day.year}"
}

//convert Long millis to a date string
fun convertMillisToDate(millis: Long?): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return if (millis == null) "" else{ formatter.format(Date(millis)) }
}

fun getHyperTensionStage(
    systolicValue: String,
    diastolicValue: String
): String {
    try {
        val diastolicValue = diastolicValue.toInt()
        val systolicValue = systolicValue.toInt()

        return if (systolicValue <= 0 || diastolicValue <= 0) {
            "Error"
        } else if (systolicValue >= 160 || diastolicValue >= 100) {
            "Grade 2"
        } else if (systolicValue >= 140 || diastolicValue >= 90) {
            "Grade 1"
        } else if (systolicValue >= 130 || diastolicValue >= 85) {
            "High Normal BP"
        } else {
            "Normal BP"
        }

    } catch (_: NumberFormatException) {
        return "Error"
    }
}