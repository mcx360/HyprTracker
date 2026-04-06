package io.github.mcx360.hyprtracker.data.Source.Local.Medication.Impl

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecordedMedicationDAO {
    @Query("SELECT * FROM RecordedMedication")
    fun getAllMedications(): List<RecordedMedication>

    @Insert
    fun insertMedication(medication: RecordedMedication)

    @Delete
    fun deleteMedication(medication: RecordedMedication)
}