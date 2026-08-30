package io.github.mcx360.hyprtracker.data

import android.content.Context
import io.github.mcx360.hyprtracker.data.source.local.AppDataBase
import io.github.mcx360.hyprtracker.data.source.local.bloodPressure.BloodPressureRepository
import io.github.mcx360.hyprtracker.data.source.local.bloodPressure.OfflineBloodPressureRepository
import io.github.mcx360.hyprtracker.data.source.local.medication.MedicationRepository
import io.github.mcx360.hyprtracker.data.source.local.medication.OfflineMedicationRepository

interface  AppContainer {
    val bloodPressureRepository: BloodPressureRepository
    val medicationRepository: MedicationRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val bloodPressureRepository: BloodPressureRepository by lazy {
        OfflineBloodPressureRepository(AppDataBase.getDatabase(context).recordedBloodPressureDAO())
    }
    override val medicationRepository: MedicationRepository by lazy {
        OfflineMedicationRepository(AppDataBase.getDatabase(context).recordedMedicationDAO())
    }
}