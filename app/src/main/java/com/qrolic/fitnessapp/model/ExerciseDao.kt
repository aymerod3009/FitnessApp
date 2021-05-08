package com.qrolic.fitnessapp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExerciseDao {

    @Insert
    suspend fun insertAll(exerciseCompleteTable: ExerciseCompleteTable):Long

    @Query("SELECT * FROM ExerciseCompleteTable where date =:date")
    suspend fun fetchTodayData(date: String?): List<ExerciseCompleteTable>

    @Query("SELECT * FROM ExerciseCompleteTable")
    suspend fun fetchAllData(): List<ExerciseCompleteTable>

}