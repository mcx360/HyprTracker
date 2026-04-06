package io.github.mcx360.hyprtracker.data.Source.Local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecordedMedication(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "medication_name") val name: String,
    @ColumnInfo(name = "medication_description") val description: String,
    @ColumnInfo(name = "medication_schedule") val schedule: String,
    @ColumnInfo(name = "medication_times_per_day") val timesPerDay: Int,
    @ColumnInfo(name = "medication_dosage") val dosePerIntake: String,
    @ColumnInfo(name = "notifications_enabled") val notificationsEnabled: Boolean,
    @ColumnInfo(name = "notifications_reminder_times") val reminders: String,
    @ColumnInfo(name = "start_day") val startDate: String,
    @ColumnInfo(name = "End_date") val endDate: String
    )