package io.github.mcx360.hyprtracker.data.Source.Local.Medication

import io.github.mcx360.hyprtracker.data.Source.Local.Medication.Impl.RecordedMedication
import kotlinx.coroutines.flow.Flow

interface MedicationRepository {

    suspend fun getAllMedicationsStream() : Flow<List<RecordedMedication>>

    suspend fun addMedication(medication: RecordedMedication)

    suspend fun removeMedication(medication: RecordedMedication)
}