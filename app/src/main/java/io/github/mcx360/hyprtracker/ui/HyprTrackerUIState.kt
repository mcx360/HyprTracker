package io.github.mcx360.hyprtracker.ui

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Date
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant



data class HyprTrackerUIState(
    val systolicValue: String = "",
    val diastolicValue: String = "",
    val pulseValue: String = "",
    val date: LocalDate = LocalDate.now(),
    val time: LocalTime = LocalTime.now().withSecond(0).withNano(0)
)
