package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SurveyDao {
    @Query("SELECT * FROM survey_records ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<SurveyRecordEntity>>

    @Query("SELECT COUNT(*) FROM survey_records")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: SurveyRecordEntity)

    @Query("DELETE FROM survey_records WHERE id = :id")
    suspend fun deleteRecordById(id: Int)

    @Query("DELETE FROM survey_records")
    suspend fun clearAll()
}
