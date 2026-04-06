package io.github.mcx360.hyprtracker.data

import android.content.Context
import io.github.mcx360.hyprtracker.data.Source.Local.AppDataBase
import io.github.mcx360.hyprtracker.data.Source.Local.BloodPressure.BloodPressureRepository
import io.github.mcx360.hyprtracker.data.Source.Local.BloodPressure.OfflineBloodPressureRepository
import io.github.mcx360.hyprtracker.data.Source.Local.Medication.MedicationRepository
import io.github.mcx360.hyprtracker.data.Source.Local.Medication.OfflineMedicationRepository

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