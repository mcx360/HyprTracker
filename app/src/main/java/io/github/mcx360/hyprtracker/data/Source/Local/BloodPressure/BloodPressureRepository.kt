package io.github.mcx360.hyprtracker.data.Source.Local.BloodPressure

import io.github.mcx360.hyprtracker.data.Source.Local.BloodPressure.Impl.RecordedBloodPressure
import kotlinx.coroutines.flow.Flow

interface BloodPressureRepository {

    suspend fun getAllRecordingsStream() : Flow<List<RecordedBloodPressure>>

    suspend fun  removeBloodPressureReading(reading: RecordedBloodPressure)

    suspend fun addBloodPressureReading(reading: RecordedBloodPressure)

    suspend fun removeAllBloodPressureReadings()
}