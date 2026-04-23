package io.github.mcx360.hyprtracker.data.Source.Local.BloodPressure

import io.github.mcx360.hyprtracker.data.Source.Local.BloodPressure.Impl.RecordedBloodPressure
import io.github.mcx360.hyprtracker.data.Source.Local.BloodPressure.Impl.RecordedBloodPressureDAO
import kotlinx.coroutines.flow.Flow

class OfflineBloodPressureRepository(private val bloodPressureDAO: RecordedBloodPressureDAO) :
    BloodPressureRepository {

    override suspend fun getAllRecordingsStream(): Flow<List<RecordedBloodPressure>> = bloodPressureDAO.getAllBloodPressureReadings()

    override suspend fun addBloodPressureReading(reading: RecordedBloodPressure) = bloodPressureDAO.insertBloodPressureReading(reading)

    override suspend fun removeBloodPressureReading(systolicValue: String, diastolicValue: String, pulseValue: String?, date: String, time: String) =
        bloodPressureDAO.deleteBloodPressureReading(systolicValue,diastolicValue,pulseValue,date,time)

    override suspend fun removeAllBloodPressureReadings() = bloodPressureDAO.deleteAllBloodPressureReadings()

}