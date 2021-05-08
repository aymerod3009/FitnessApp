package com.qrolic.fitnessapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RemiderTable")
data class RemiderTable (

    @PrimaryKey(autoGenerate = true)
    var id : Int,

    @ColumnInfo(name = "time") var time: String,
    @ColumnInfo(name = "switch") var switchReminder: Boolean,
    @ColumnInfo(name = "monday") var monday: Boolean,
    @ColumnInfo(name = "tuesday") var tuesday: Boolean,
    @ColumnInfo(name = "wednesday") var wednesday: Boolean,
    @ColumnInfo(name = "thursday") var thursday: Boolean,
    @ColumnInfo(name = "friday") var friday: Boolean,
    @ColumnInfo(name = "saturday") var saturday: Boolean,
    @ColumnInfo(name = "sunday") var sunday: Boolean

)

