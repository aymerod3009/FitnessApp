package com.qrolic.fitnessapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ExerciseCompleteTable")
data class ExerciseCompleteTable (
    @PrimaryKey(autoGenerate = true)
    var id : Int,
    @ColumnInfo(name = "totalTime") var totalTime: String,
    @ColumnInfo(name = "burnedCalories") var burnedCalories: String,
    @ColumnInfo(name = "totalExercise") var totalExercise: String,
        @ColumnInfo(name = "workoutTypeName") var workoutTypeName: String,
    @ColumnInfo(name = "dateTime") var dateTime:String,
    @ColumnInfo(name = "date") var date:String
)

