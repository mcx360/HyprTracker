package io.github.mcx360.hyprtracker.data.Local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordedBloodPressureDAO{
    @Query("SELECT * FROM RecordedBloodPressureReadings")
    fun getAllBloodPressureReadings(): Flow<List<RecordedBloodPressure>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBloodPressureReading(bloodPressureReading: RecordedBloodPressure)

    @Delete
    fun deleteBloodPressureReading(bloodPressureReading: RecordedBloodPressure)
}