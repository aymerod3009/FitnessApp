package com.qrolic.fitnessapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity(tableName = "WorkoutNameTable")
data class WorkoutNameTable (
    @ColumnInfo(name = "workoutName") var workoutName: String,
    @ColumnInfo(name = "workoutTypeList") var workoutTypeList: List<WorkoutTypeTable>
)