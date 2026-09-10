package io.github.mcx360.hyprtracker.ui.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//convert Long millis to a date string
fun convertMillisToDate(millis: Long?): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return if (millis == null) "" else{ formatter.format(Date(millis)) }
}
