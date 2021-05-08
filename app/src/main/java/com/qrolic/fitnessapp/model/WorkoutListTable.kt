package com.qrolic.fitnessapp.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import java.io.Serializable

@Entity(tableName = "WorkoutListTable")
data class WorkoutListTable(
    @ColumnInfo(name = "execriseName") var execriseName: String?,
    @ColumnInfo(name = "workoutTypeImage") var workoutTypeImage: String?,
    @ColumnInfo(name = "execrsieTime") var execrsieTime: String?,
    @ColumnInfo(name = "execrsieMET") var execrsieMET: String?,
    @ColumnInfo(name = "exerciseDesc") var exerciseDesc:String
):Serializable