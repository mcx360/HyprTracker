package io.github.mcx360.hyprtracker.data.source.local.bloodPressure

import io.github.mcx360.hyprtracker.data.source.local.bloodPressure.impl.RecordedBloodPressure
import io.github.mcx360.hyprtracker.data.source.local.bloodPressure.impl.RecordedBloodPressureDAO
import io.github.mcx360.hyprtracker.ui.model.getHyperTensionStage
import kotlinx.coroutines.flow.Flow
import java.io.InputStream

class OfflineBloodPressureRepository(private val bloodPressureDAO: RecordedBloodPressureDAO) : BloodPressureRepository {

    override suspend fun getAllRecordingsStream(): Flow<List<RecordedBloodPressure>> = bloodPressureDAO.getAllBloodPressureReadings()

    override suspend fun addBloodPressureReading(reading: RecordedBloodPressure) = bloodPressureDAO.insertBloodPressureReading(reading)

    override suspend fun importBloodPressureLogs(stream: InputStream) {
        removeAllBloodPressureReadings()
        val logs: MutableList<RecordedBloodPressure> = mutableListOf()

        stream.bufferedReader().forEachLine {
           val record = it.split(",")
            val bpRecord = RecordedBloodPressure(
                dateAdded = record[0],
                timeAdded = record[1],
                systolicValue = record[2].toInt(),
                diastolicValue = record[3].toInt(),
                pulseValue = record[4].toInt(),
                noteValue = record[5],
                hypertensionStage = getHyperTensionStage(record[2], record[3])
            )
            logs.add(bpRecord)
        }
        logs.forEach {
            addBloodPressureReading(it)
        }
    }

    override suspend fun removeBloodPressureReading(reading: RecordedBloodPressure) = bloodPressureDAO.deleteBloodPressureReading(reading)

    override suspend fun removeAllBloodPressureReadings() = bloodPressureDAO.deleteAllBloodPressureReadings()

    override suspend fun getSystolicAverage(startDate: String?, endDate: String?): Int = bloodPressureDAO.getSystolicAverage(startDate,endDate)?.toInt() ?: 0

    override suspend fun getSystolicMax(startDate: String?, endDate: String?): Int = bloodPressureDAO.getSystolicMax(startDate,endDate)?.toInt() ?: 0

    override suspend fun getSystolicMin(startDate: String?, endDate: String?): Int = bloodPressureDAO.getSystolicMin(startDate,endDate)?.toInt() ?: 0

    override suspend fun getDiastolicAverage(startDate: String?, endDate: String?): Int = bloodPressureDAO.getDiastolicAverage(startDate, endDate)?.toInt() ?: 0

    override suspend fun getDiastolicMax(startDate: String?, endDate: String?): Int = bloodPressureDAO.getDiastolicMax(startDate, endDate)?.toInt() ?: 0

    override suspend fun getDiastolicMin(startDate: String?, endDate: String?): Int = bloodPressureDAO.getDiastolicMin(startDate, endDate)?.toInt() ?: 0

    override suspend fun getPulseAverage(startDate: String?, endDate: String?): Int = bloodPressureDAO.getPulseAverage(startDate,endDate)?.toInt() ?: 0

    override suspend fun getPulseMax(startDate: String?, endDate: String?): Int = bloodPressureDAO.getPulseMax(startDate, endDate)?.toInt() ?: 0

    override suspend fun getPulseMin(startDate: String?, endDate: String?): Int = bloodPressureDAO.getPulseMin(startDate, endDate)?.toInt() ?: 0

    override suspend fun getOldestDate(): String? = bloodPressureDAO.getOldestDate()

    override suspend fun hasRecords(): Boolean = bloodPressureDAO.hasRecords()

    override suspend fun getStages(startDate: String?, endDate: String?): List<Float> {
        val counts = mutableListOf(0f, 0f, 0f, 0f)
        var total = 0
        var index= 0
        bloodPressureDAO.getStages(startDate, endDate).forEach {
            when(it){
                "Normal BP" -> counts[0]++  //hard coded temporarily
                "High Normal BP" -> counts[1]++
                "Grade 1" -> counts[2]++
                "Grade 2" -> counts[3]++
            }
            total++
        }
        counts.forEach { if (counts[index] != 0f) counts[index] = it/total * 100; index++}
        return counts
    }
}