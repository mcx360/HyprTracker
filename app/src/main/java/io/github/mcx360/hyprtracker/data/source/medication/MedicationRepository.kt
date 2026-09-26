package io.github.mcx360.hyprtracker.data.source.medication

import io.github.mcx360.hyprtracker.data.source.medication.impl.RecordedMedication
import io.github.mcx360.hyprtracker.data.source.medication.impl.RecordedMedicationDAO
import kotlinx.coroutines.flow.Flow

class MedicationRepository(private val medicationDAO: RecordedMedicationDAO) {
    suspend fun getAllMedicationsStream(): Flow<List<RecordedMedication>> = medicationDAO.getAllMedications()

    suspend fun addMedication(medication: RecordedMedication) = medicationDAO.insertMedication(medication)

    suspend fun removeMedication(medication: RecordedMedication) = medicationDAO.deleteMedication(medication)

    suspend fun removeAllMedications() = medicationDAO.deleteAllMedications()
}