package io.github.mcx360.hyprtracker.data.Source.Local.Medication

import io.github.mcx360.hyprtracker.data.Source.Local.Medication.Impl.RecordedMedication
import io.github.mcx360.hyprtracker.data.Source.Local.Medication.Impl.RecordedMedicationDAO

class OfflineMedicationRepository(private val medicationDAO: RecordedMedicationDAO) : MedicationRepository {
    override suspend fun getAllMedications(): List<RecordedMedication> = medicationDAO.getAllMedications()

    override suspend fun addMedication(medication: RecordedMedication) = medicationDAO.insertMedication(medication)

    override suspend fun removeMedication(medication: RecordedMedication) = medicationDAO.deleteMedication(medication)

}