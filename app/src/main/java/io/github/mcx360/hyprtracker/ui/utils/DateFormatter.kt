package io.github.mcx360.hyprtracker.ui.utils

//format date from internal database format YYYY-MM-DD to regular format DD/MM/YYYY
fun formatToRegularDate(date: String) : String{
    val dateInfo = date.split('-')
    return "${dateInfo[2]}/${dateInfo[1]}/${dateInfo[0]}"
}