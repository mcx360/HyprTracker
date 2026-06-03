package io.github.mcx360.hyprtracker.ui.model

data class Medicine(
    val name: String,
    val description: String,
    val schedule: String,
    val scheduledDays: Set<String>,
    val timesPerDay: Int,
    val dosePerIntake: String,
    val notificationsEnabled: Boolean,
    val scheduledNotificationsTime: List<String>,
    val startDate: String,
    val endDate: String
)