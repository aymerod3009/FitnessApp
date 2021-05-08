package com.qrolic.fitnessapp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update

@Dao
interface ProfileDao {

    @Insert
    suspend fun insertAll(profileTable: ProfileTable):Long

    @Update
    suspend fun updateAll(profileTable: ProfileTable):Int

}