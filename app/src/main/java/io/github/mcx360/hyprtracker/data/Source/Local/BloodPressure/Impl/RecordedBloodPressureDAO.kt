package io.github.mcx360.hyprtracker.data.Source.Local.BloodPressure.Impl

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordedBloodPressureDAO {
    @Query("SELECT * FROM RecordedBloodPressureReadings ORDER BY date_added DESC, time_added DESC")
    fun getAllBloodPressureReadings(): Flow<List<RecordedBloodPressure>>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertBloodPressureReading(bloodPressureReading: RecordedBloodPressure)

    @Delete
    suspend fun deleteBloodPressureReading(bloodPressure: RecordedBloodPressure)

    @Query("DELETE FROM RecordedBloodPressureReadings")
    suspend fun deleteAllBloodPressureReadings()
}