package com.example.sowitplot.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.sowitplot.ui.PlotViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

// ---------- Fake in-memory DAO used only for tests ----------
class FakePlotDao : PlotDao {

    private val items = mutableListOf<PlotEntity>()
    private val flow = MutableStateFlow<List<PlotEntity>>(emptyList())

    override fun getAllPlots(): Flow<List<PlotEntity>> = flow

    override suspend fun insert(plot: PlotEntity): Long {
        val newId = (items.maxOfOrNull { it.id } ?: 0L) + 1
        val withId = plot.copy(id = newId)
        items.add(withId)
        flow.value = items.toList()
        return newId
    }

    override suspend fun update(plot: PlotEntity) {
        val index = items.indexOfFirst { it.id == plot.id }
        if (index >= 0) {
            items[index] = plot
            flow.value = items.toList()
        }
    }

    override suspend fun delete(plot: PlotEntity) {
        items.removeAll { it.id == plot.id }
        flow.value = items.toList()
    }

    override suspend fun rename(id: Long, newName: String) {
        val index = items.indexOfFirst { it.id == id }
        if (index >= 0) {
            val p = items[index]
            items[index] = p.copy(name = newName)
            flow.value = items.toList()
        }
    }
}

// ---------- ViewModel unit test ----------
@OptIn(ExperimentalCoroutinesApi::class)
class PlotViewModelTest {

    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    private val dispatcher = StandardTestDispatcher()
    private lateinit var dao: FakePlotDao
    private lateinit var viewModel: PlotViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        dao = FakePlotDao()
        viewModel = PlotViewModel(dao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun savePlot_insertsIntoDao() = runTest {
        // when
        viewModel.savePlot(
            name = "Test plot",
            encoded = "encoded_poly",
            centerLat = 1.0,
            centerLng = 2.0,
            areaSqMeters = 1000.0,
            thumbnailPath = null
        )

        // let the coroutine finish
        dispatcher.scheduler.advanceUntilIdle()

        val list = dao.getAllPlots().first()

        assertEquals(1, list.size)
        assertEquals("Test plot", list[0].name)
    }
}
