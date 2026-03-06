package io.github.mcx360.hyprtracker.ui

import io.github.mcx360.hyprtracker.data.FakeData
import io.github.mcx360.hyprtracker.data.HyprReading
import java.time.LocalDate
import java.time.LocalTime

data class HyprTrackerUIState(
    val systolicValue: String = "",
    val diastolicValue: String = "",
    val pulseValue: String = "",
    val date: String = LocalDate.now().toString(),
    val time: String = LocalTime.now().withSecond(0).withNano(0).toString(),
    val notes: String = "",
    val readings: List<HyprReading> = FakeData.getInitialReadings()
)
