package io.github.mcx360.hyprtracker.data.Source.Local.BloodPressure

import io.github.mcx360.hyprtracker.data.Source.Local.BloodPressure.Impl.RecordedBloodPressure
import kotlinx.coroutines.flow.Flow

interface BloodPressureRepository {

    fun getAllRecordingsStream() : Flow<List<RecordedBloodPressure>>

    suspend fun  removeBloodPressureReading(systolicValue: String, diastolicValue: String, pulseValue: String?, date: String, time: String)

    suspend fun addBloodPressureReading(reading: RecordedBloodPressure)

}