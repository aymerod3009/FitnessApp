package com.qrolic.fitnessapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.qrolic.fitnessapp.model.*


@Database(entities = arrayOf(ProfileTable::class,ExerciseCompleteTable::class,RemiderTable::class),version = 6)
abstract class FitnessDatabase:RoomDatabase(){

    abstract fun profileDao(): ProfileDao
    abstract fun exerciseComplete():ExerciseDao
    abstract fun reminderDao():ReminderDao

    companion object {
        @Volatile private var instance: FitnessDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            FitnessDatabase::class.java, "fitness.db")
            .fallbackToDestructiveMigration()
            .build()
    }

}