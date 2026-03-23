package io.github.mcx360.hyprtracker.data.Source.Local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RecordedBloodPressureReadings")
data class RecordedBloodPressure(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val  dateAdded: String,
    val timeAdded: String,
    @ColumnInfo(name = "systolic_value") val systolicValue: String,
    @ColumnInfo(name = "diastolic_Value") val diastolicValue: String,
    @ColumnInfo(name = "pulse_value") val pulseValue: String?,
    @ColumnInfo(name = "note_value") val noteValue: String?,
    @ColumnInfo(name = "hypertension_stage") val hypertensionStage: String
)