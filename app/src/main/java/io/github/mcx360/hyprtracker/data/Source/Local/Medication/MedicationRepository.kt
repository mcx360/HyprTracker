package io.github.mcx360.hyprtracker.data.Source.Local.Medication

import io.github.mcx360.hyprtracker.data.Source.Local.Medication.Impl.RecordedMedication

interface MedicationRepository {

    fun getAllMedications() : List<RecordedMedication>

    suspend fun addMedication(medication: RecordedMedication)

    suspend fun removeMedication(medication: RecordedMedication)
}