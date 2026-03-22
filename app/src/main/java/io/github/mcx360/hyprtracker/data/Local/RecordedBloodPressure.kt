package io.github.mcx360.hyprtracker.data.Local

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["dateAdded", "timeAdded"], tableName = "RecordedBloodPressureReadings")
data class RecordedBloodPressure(
    val  dateAdded: String,
    val timeAdded: String,
    @ColumnInfo(name = "systolic_value") val systolicValue: String,
    @ColumnInfo(name = "diastolic_Value") val diastolicValue: String,
    @ColumnInfo(name = "pulse_value") val pulseValue: String?,
    @ColumnInfo(name = "note_value") val noteValue: String?
)