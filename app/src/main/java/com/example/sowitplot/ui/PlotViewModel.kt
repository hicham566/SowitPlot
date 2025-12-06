package com.example.sowitplot.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.sowitplot.data.PlotDao
import com.example.sowitplot.data.PlotEntity
import kotlinx.coroutines.launch

class PlotViewModel(private val dao: PlotDao) : ViewModel() {

    val plots = dao.getAllPlots().asLiveData()

    fun savePlot(name: String, encoded: String, centerLat: Double, centerLng: Double) {
        viewModelScope.launch {
            val entity = PlotEntity(
                name = name,
                polygonEncoded = encoded,
                centerLat = centerLat,
                centerLng = centerLng
            )
            dao.insert(entity)
        }
    }
}

class PlotViewModelFactory(private val dao: PlotDao) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlotViewModel::class.java)) {
            return PlotViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
