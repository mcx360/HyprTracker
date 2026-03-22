package io.github.mcx360.hyprtracker.data

import io.github.mcx360.hyprtracker.data.Local.RecordedBloodPressure
import kotlinx.coroutines.flow.Flow

interface BloodPressureRepository {

    fun getAllRecordingsStream() : Flow<List<RecordedBloodPressure>>

    fun  removeBloodPressureReading(reading: RecordedBloodPressure)

    fun addBloodPressureReading(reading: RecordedBloodPressure)

}