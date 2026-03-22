package io.github.mcx360.hyprtracker.data

import io.github.mcx360.hyprtracker.data.Source.Local.RecordedBloodPressure
import io.github.mcx360.hyprtracker.data.Source.Local.RecordedBloodPressureDAO
import kotlinx.coroutines.flow.Flow

class OfflineBloodPressureRepository(private val bloodPressureDAO: RecordedBloodPressureDAO) : BloodPressureRepository {

    override fun getAllRecordingsStream(): Flow<List<RecordedBloodPressure>> = bloodPressureDAO.getAllBloodPressureReadings()

    override suspend fun addBloodPressureReading(reading: RecordedBloodPressure) = bloodPressureDAO.insertBloodPressureReading(reading)

    override suspend fun removeBloodPressureReading(reading: RecordedBloodPressure) = bloodPressureDAO.deleteBloodPressureReading(reading)
}