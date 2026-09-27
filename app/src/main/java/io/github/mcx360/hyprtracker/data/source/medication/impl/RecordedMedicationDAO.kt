package io.github.mcx360.hyprtracker.data.source.medication.impl

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordedMedicationDAO {
    @Query("SELECT * FROM RecordedMedication")
    fun getAllMedications(): Flow<List<RecordedMedication>>

    @Insert
    suspend fun insertMedication(medication: RecordedMedication)

    @Delete
    suspend fun deleteMedication(medication: RecordedMedication)

    @Query("DELETE FROM RecordedMedication")
    suspend fun deleteAllMedications()
}