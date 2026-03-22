package io.github.mcx360.hyprtracker.data

import io.github.mcx360.hyprtracker.data.Source.Local.RecordedBloodPressure
import kotlinx.coroutines.flow.Flow

interface BloodPressureRepository {

    fun getAllRecordingsStream() : Flow<List<RecordedBloodPressure>>

    suspend fun  removeBloodPressureReading(reading: RecordedBloodPressure)

    suspend fun addBloodPressureReading(reading: RecordedBloodPressure)

}