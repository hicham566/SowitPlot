package com.example.sowitplot.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plots")
data class PlotEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val polygonEncoded: String,
    val centerLat: Double,
    val centerLng: Double
)
