package com.qrolic.fitnessapp.model

import androidx.room.*

@Dao
interface ReminderDao {

    @Insert
    suspend fun insertAll(remiderTable: RemiderTable):Long

    @Query("SELECT * FROM RemiderTable")
    suspend fun fetchAllData(): List<RemiderTable>

    @Update
    suspend fun updateAll(remiderTable: RemiderTable):Int

    @Delete
    suspend fun deleteReminderData(remiderTable: RemiderTable): Int
}