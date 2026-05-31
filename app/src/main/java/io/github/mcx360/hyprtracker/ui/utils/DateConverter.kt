package io.github.mcx360.hyprtracker.ui.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//convert Long millis to a date string
fun convertMillisToDate(millis: Long?): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return if (millis == null){
        ""
    } else{
        formatter.format(Date(millis))
    }
}

//Convert date string in format YYYY-MM-DD to millis
fun convertDateToMillis(dateString: String?): Long? {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return if (dateString.isNullOrEmpty()) {
        null
    } else {
        try {
            formatter.parse(dateString)?.time
        } catch (_: Exception) {
            null
        }
    }
}