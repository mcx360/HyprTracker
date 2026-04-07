package io.github.mcx360.hyprtracker.ui.medicineScreen

import java.time.LocalDate

data class MedicineState(
    val medicationName: String = "",
    val medicationDescription: String = "",
    val medicationSchedule: String = "",
    val medicationTimesPerDay: Int = 0,
    val medicationIntake: Int = 0,
    val medicationDosage: String = "",
    val medicationSelectedDays: Set<String> = setOf(),
    val medicationReminderTimes: List<String> = listOf("","","","","","",""),
    val medicationEndDate: String = "",
    var medicineList: List<String> = listOf(),
    val medicationNotifications: Boolean = false,
    val date: String = LocalDate.now().toString()
)
