package com.qrolic.fitnessapp.model


import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "WorkoutNameTable")
data class WorkoutTypeTable (
    @ColumnInfo(name = "workoutTypeName") var workoutTypeName: String,
    @ColumnInfo(name = "workoutTypeImage") var workoutTypeImage: Int,
    @ColumnInfo(name = "workoutTypeTime") var workoutTypeTime: String,
    @ColumnInfo(name = "workoutType") var workoutType: String
)