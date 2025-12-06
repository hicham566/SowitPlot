package com.example.sowitplot

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sowitplot.data.PlotDatabase
import com.example.sowitplot.databinding.ActivityMainBinding
import com.example.sowitplot.ui.PlotViewModel
import com.example.sowitplot.ui.PlotViewModelFactory
import com.example.sowitplot.util.LatLngEncoding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var map: GoogleMap
    private lateinit var viewModel: PlotViewModel

    private val currentPoints = mutableListOf<LatLng>()
    private var currentPolygon: Polygon? = null
    private val pointMarkers = mutableListOf<Marker>()
    private var isDrawing: Boolean = true // drawing enabled by default

    private data class SpinnerItem(val id: Long, val name: String) {
        override fun toString(): String = name
    }

    private val spinnerItems = mutableListOf<SpinnerItem>()
    private var spinnerAdapter: ArrayAdapter<SpinnerItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val db = PlotDatabase.getInstance(applicationContext)
        val factory = PlotViewModelFactory(db.plotDao())
        viewModel = ViewModelProvider(this, factory)[PlotViewModel::class.java]

        setupUi()
        observePlots()
    }

    private fun setupUi() {
        spinnerItems.clear()
        spinnerItems.add(SpinnerItem(PLACEHOLDER_ID, "Select plot"))

        spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            spinnerItems
        )
        binding.spinnerPlots.adapter = spinnerAdapter

        binding.btnToggleDraw.text = getString(R.string.stop_drawing)

        binding.btnToggleDraw.setOnClickListener {
            isDrawing = !isDrawing
            binding.btnToggleDraw.text = if (isDrawing) {
                getString(R.string.stop_drawing)
            } else {
                getString(R.string.start_drawing)
            }
        }

        binding.btnClear.setOnClickListener {
            clearCurrentPolygon()
        }

        binding.btnSavePlot.setOnClickListener {
            saveCurrentPlot()
        }

        binding.spinnerPlots.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent == null) return
                val item = parent.getItemAtPosition(position) as SpinnerItem
                if (item.id == PLACEHOLDER_ID) return

                loadPlotFromSpinnerItem(item)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun observePlots() {
        viewModel.plots.observe(this) { plots ->
            spinnerItems.clear()
            spinnerItems.add(SpinnerItem(PLACEHOLDER_ID, "Select plot"))

            plots.forEach { plot ->
                spinnerItems.add(SpinnerItem(plot.id, plot.name))
            }
            spinnerAdapter?.notifyDataSetChanged()
            binding.spinnerPlots.setSelection(0, false)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true

        map.setOnMapClickListener { latLng ->
            handleMapTap(latLng)
        }

        map.setOnMapLongClickListener { latLng ->
            handleMapTap(latLng)
        }

        val defaultLatLng = LatLng(33.5731, -7.5898)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, 9f))
    }

    private fun handleMapTap(latLng: LatLng) {
        if (!isDrawing) return
        currentPoints.add(latLng)
        addMarker(latLng)
        drawPolygon()
    }

    private fun addMarker(latLng: LatLng) {
        val marker = map.addMarker(
            MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        )
        if (marker != null) {
            pointMarkers.add(marker)
        }
    }

    private fun clearMarkers() {
        pointMarkers.forEach { it.remove() }
        pointMarkers.clear()
    }

    private fun drawPolygon() {
        currentPolygon?.remove()
        if (currentPoints.size < 2) return

        val polygonOptions = PolygonOptions()
            .addAll(currentPoints)
            .strokeWidth(4f)
            .strokePattern(listOf(Dash(20f), Gap(10f)))
            .strokeColor(0xFF4A148C.toInt())
            .fillColor(0x334A148C)

        currentPolygon = map.addPolygon(polygonOptions)
    }

    private fun clearCurrentPolygon() {
        currentPoints.clear()
        currentPolygon?.remove()
        currentPolygon = null
        clearMarkers()

        binding.spinnerPlots.setSelection(0, false)
    }

    private fun saveCurrentPlot() {
        if (currentPoints.size < 3) {
            Toast.makeText(
                this,
                "Add at least 3 points to form a polygon",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val input = android.widget.EditText(this)
        input.hint = getString(R.string.enter_plot_name)

        AlertDialog.Builder(this)
            .setTitle(R.string.save_plot)
            .setView(input)
            .setPositiveButton(R.string.ok) { _, _ ->
                val name = input.text.toString().ifBlank { "Plot ${System.currentTimeMillis()}" }
                val encoded = LatLngEncoding.encode(currentPoints)
                val center = LatLngEncoding.computeCenter(currentPoints)


                Toast.makeText(this, "Saving ${currentPoints.size} points", Toast.LENGTH_SHORT).show()

                viewModel.savePlot(name, encoded, center.latitude, center.longitude)
                Toast.makeText(this, "Plot saved", Toast.LENGTH_SHORT).show()
            }

            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun loadPlotFromSpinnerItem(item: SpinnerItem) {
        val plots = viewModel.plots.value ?: return
        val selected = plots.firstOrNull { it.id == item.id } ?: return
        val points = LatLngEncoding.decode(selected.polygonEncoded)

        Toast.makeText(this, "Loaded ${points.size} points", Toast.LENGTH_SHORT).show()

        if (points.isEmpty()) return

        currentPoints.clear()
        currentPoints.addAll(points)
        clearMarkers()
        points.forEach { addMarker(it) }
        drawPolygon()

        val center = LatLng(selected.centerLat, selected.centerLng)
        val update = CameraUpdateFactory.newLatLngZoom(center, 11f)


        map.animateCamera(update, 2000, null)

    }


    companion object {
        private const val PLACEHOLDER_ID = -1L
    }
}
