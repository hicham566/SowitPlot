package com.example.sowitplot.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlotDao {

    @Query("SELECT * FROM plots ORDER BY id DESC")
    fun getAllPlots(): Flow<List<PlotEntity>>

    @Insert
    suspend fun insert(plot: PlotEntity): Long

    @Update
    suspend fun update(plot: PlotEntity)

    @Delete
    suspend fun delete(plot: PlotEntity)

    @Query("UPDATE plots SET name = :newName WHERE id = :id")
    suspend fun rename(id: Long, newName: String)
}
