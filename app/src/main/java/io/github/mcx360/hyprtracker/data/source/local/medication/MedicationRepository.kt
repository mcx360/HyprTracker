package io.github.mcx360.hyprtracker.data.source.local.medication

import io.github.mcx360.hyprtracker.data.source.local.medication.impl.RecordedMedication
import kotlinx.coroutines.flow.Flow

interface MedicationRepository {

    suspend fun getAllMedicationsStream() : Flow<List<RecordedMedication>>

    suspend fun addMedication(medication: RecordedMedication)

    suspend fun removeMedication(medication: RecordedMedication)

    suspend fun removeAllMedications()
}