package com.example.sowitplot.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlotDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plot: PlotEntity)

    @Query("SELECT * FROM plots ORDER BY name")
    fun getAllPlots(): Flow<List<PlotEntity>>

    @Query("SELECT * FROM plots WHERE id = :id")
    suspend fun getPlotById(id: Long): PlotEntity?
}
