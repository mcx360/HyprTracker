package io.github.mcx360.hyprtracker.ui

import java.time.LocalDate
import java.time.LocalTime

data class HyprTrackerUIState(
    val systolicValue: String = "",
    val diastolicValue: String = "",
    val pulseValue: String = "",
    val date: String = LocalDate.now().toString(),
    val time: String = LocalTime.now().withNano(0).toString(),
    val notes: String = "",
    var readings: List<HyprReading> = listOf(),
)
