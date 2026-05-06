package io.github.mcx360.hyprtracker.data.Source.Local.BloodPressure.Impl

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RecordedBloodPressureReadings", primaryKeys = ["date_added", "time_added","systolic_value","diastolic_value"])
data class RecordedBloodPressure(
    @ColumnInfo(name = "date_added") val  dateAdded: String,
    @ColumnInfo(name = "time_added")val timeAdded: String,
    @ColumnInfo(name = "systolic_value") val systolicValue: String,
    @ColumnInfo(name = "diastolic_value") val diastolicValue: String,
    @ColumnInfo(name = "pulse_value") val pulseValue: String?,
    @ColumnInfo(name = "note_value") val noteValue: String?,
    @ColumnInfo(name = "hypertension_stage") val hypertensionStage: String
)