package io.github.mcx360.hyprtracker.data.Source.Local.BloodPressure

import io.github.mcx360.hyprtracker.data.Source.Local.BloodPressure.Impl.RecordedBloodPressure
import kotlinx.coroutines.flow.Flow

interface BloodPressureRepository {

    suspend fun getAllRecordingsStream() : Flow<List<RecordedBloodPressure>>

    suspend fun  removeBloodPressureReading(reading: RecordedBloodPressure)

    suspend fun addBloodPressureReading(reading: RecordedBloodPressure)

    suspend fun removeAllBloodPressureReadings()

    suspend fun getSystolicAverage(startDate: String?, endDate: String?) : Int

    suspend fun getSystolicMax(startDate: String?, endDate: String?) : Int

    suspend fun getSystolicMin(startDate: String?, endDate: String?) : Int

    suspend fun getDiastolicAverage(startDate: String?, endDate: String?) : Int

    suspend fun getDiastolicMax(startDate: String?, endDate: String?) : Int

    suspend fun getDiastolicMin(startDate: String?, endDate: String?) : Int

    suspend fun getPulseAverage(startDate: String?, endDate: String?) : Int

    suspend fun getPulseMax(startDate: String?, endDate: String?) : Int

    suspend fun getPulseMin(startDate: String?, endDate: String?) : Int

    suspend fun getOldestDate() : String?

    suspend fun hasRecords(): Boolean

    suspend fun getStages(startDate: String?, endDate: String?): List<Float>

}