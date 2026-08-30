package io.github.mcx360.hyprtracker.data.source.local.medication

import io.github.mcx360.hyprtracker.data.source.local.medication.impl.RecordedMedication
import io.github.mcx360.hyprtracker.data.source.local.medication.impl.RecordedMedicationDAO
import kotlinx.coroutines.flow.Flow

class OfflineMedicationRepository(private val medicationDAO: RecordedMedicationDAO) : MedicationRepository {
    override suspend fun getAllMedicationsStream(): Flow<List<RecordedMedication>> = medicationDAO.getAllMedications()

    override suspend fun addMedication(medication: RecordedMedication) = medicationDAO.insertMedication(medication)

    override suspend fun removeMedication(medication: RecordedMedication) = medicationDAO.deleteMedication(medication)

    override suspend fun removeAllMedications() = medicationDAO.deleteAllMedications()

}