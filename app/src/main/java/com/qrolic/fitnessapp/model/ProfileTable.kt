package com.qrolic.fitnessapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ProfileTable")
data class ProfileTable (

    @PrimaryKey(autoGenerate = true)
    var id : Int,

    @ColumnInfo(name = "height") var height: String,
    @ColumnInfo(name = "weight") var weight: String,
    @ColumnInfo(name = "birth_date") var birthDate: String,
    @ColumnInfo(name = "type") var type: String

)

