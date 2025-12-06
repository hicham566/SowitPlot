package com.example.sowitplot.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [PlotEntity::class],
    version = 2,
    exportSchema = false
)
abstract class PlotDatabase : RoomDatabase() {

    abstract fun plotDao(): PlotDao

    companion object {
        @Volatile
        private var INSTANCE: PlotDatabase? = null

        fun getInstance(context: Context): PlotDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlotDatabase::class.java,
                    "plots_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
