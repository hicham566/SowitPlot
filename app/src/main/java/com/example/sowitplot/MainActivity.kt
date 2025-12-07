package com.example.sowitplot

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sowitplot.data.PlotDatabase
import com.example.sowitplot.data.PlotEntity
import com.example.sowitplot.databinding.ActivityMainBinding
import com.example.sowitplot.ui.PlotViewModel
import com.example.sowitplot.ui.PlotViewModelFactory
import com.example.sowitplot.util.LatLngEncoding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.maps.android.SphericalUtil
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import androidx.lifecycle.Observer
class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var map: GoogleMap
    private lateinit var viewModel: PlotViewModel

    private val currentPoints = mutableListOf<LatLng>()
    private var currentPolygon: Polygon? = null
    private val pointMarkers = mutableListOf<Marker>()
    private var isDrawing: Boolean = true

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
        // ---------- spinner ----------
        spinnerItems.clear()
        spinnerItems.add(SpinnerItem(PLACEHOLDER_ID, "Select plot"))

        spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            spinnerItems
        )
        binding.spinnerPlots.adapter = spinnerAdapter


        isDrawing = true

        binding.btnToggleDraw.setOnClickListener {
            isDrawing = !isDrawing

            val msg = if (isDrawing) {
                "Drawing enabled"
            } else {
                "Drawing disabled"
            }
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()



             val iconRes = if (isDrawing) R.drawable.icon_draw_green else R.drawable.icon_draw_off
             binding.btnToggleDraw.setImageResource(iconRes)
        }

        binding.btnClear.setOnClickListener { clearCurrentPolygon() }
        binding.btnSavePlot.setOnClickListener { saveCurrentPlot() }

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

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
        binding.btnOpenPlotsBottomSheet.setOnClickListener {
            showPlotsBottomSheet()
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

        map.setOnMapClickListener { latLng -> handleMapTap(latLng) }
        map.setOnMapLongClickListener { latLng -> handleMapTap(latLng) }

        val defaultLatLng = LatLng(33.5731, -7.5898) // Casablanca
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
                .icon(pointIcon)
                .anchor(0.5f, 0.5f)
                .zIndex(10f)
        )
        if (marker != null) pointMarkers.add(marker)
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

        val input = EditText(this)
        input.hint = getString(R.string.enter_plot_name)

        AlertDialog.Builder(this)
            .setTitle(R.string.save_plot)
            .setView(input)
            .setPositiveButton(R.string.ok) { _, _ ->
                val name = input.text.toString().ifBlank {
                    "Plot ${System.currentTimeMillis()}"
                }

                val encoded = LatLngEncoding.encode(currentPoints)
                val center = LatLngEncoding.computeCenter(currentPoints)
                val areaSqMeters = SphericalUtil.computeArea(currentPoints)

                captureSnapshot { path ->
                    viewModel.savePlot(
                        name = name,
                        encoded = encoded,
                        centerLat = center.latitude,
                        centerLng = center.longitude,
                        areaSqMeters = areaSqMeters,
                        thumbnailPath = path
                    )
                    Toast.makeText(this, "Plot saved", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun captureSnapshot(onSaved: (String?) -> Unit) {
        map.snapshot { bitmap ->
            if (bitmap == null) {
                onSaved(null)
                return@snapshot
            }
            try {
                val dir = File(getExternalFilesDir(null), "plot_thumbnails")
                if (!dir.exists()) dir.mkdirs()
                val file = File(dir, "plot_${System.currentTimeMillis()}.png")
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
                }
                onSaved(file.absolutePath)
            } catch (e: Exception) {
                e.printStackTrace()
                onSaved(null)
            }
        }
    }


    private fun loadPlotFromSpinnerItem(item: SpinnerItem) {
        val plots = viewModel.plots.value ?: return
        val selected = plots.firstOrNull { it.id == item.id } ?: return
        loadPlotOnMap(selected)
    }

    private fun loadPlotOnMap(plot: PlotEntity) {
        val points = LatLngEncoding.decode(plot.polygonEncoded)
        if (points.isEmpty()) return

        currentPoints.clear()
        currentPoints.addAll(points)
        clearMarkers()
        points.forEach { addMarker(it) }
        drawPolygon()

        val center = LatLng(plot.centerLat, plot.centerLng)
        val update = CameraUpdateFactory.newLatLngZoom(center, 11f)
        map.animateCamera(update, 2000, null)
    }


    private fun showPlotsBottomSheet() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_plots, null)
        dialog.setContentView(view)

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerPlots)
        recycler.layoutManager = LinearLayoutManager(this)

        val adapter = PlotsAdapter(
            onSelect = { plot ->
                loadPlotOnMap(plot)
                dialog.dismiss()
            },
            onEdit = { plot ->
                showRenameDialog(plot)
            },
            onDelete = { plot ->
                confirmDeletePlot(plot)
            }
        )
        recycler.adapter = adapter


        val observer = Observer<List<PlotEntity>> { plots ->
            adapter.submitList(plots)
        }
        viewModel.plots.observe(this, observer)


        dialog.setOnDismissListener {
            viewModel.plots.removeObserver(observer)
        }

        dialog.show()
    }


    private fun showRenameDialog(plot: PlotEntity) {
        val input = EditText(this)
        input.setText(plot.name)

        AlertDialog.Builder(this)
            .setTitle("Rename plot")
            .setView(input)
            .setPositiveButton(R.string.ok) { _, _ ->
                val newName = input.text.toString().trim()
                if (newName.isNotEmpty()) {
                    viewModel.renamePlot(plot, newName)
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun confirmDeletePlot(plot: PlotEntity) {
        AlertDialog.Builder(this)
            .setTitle("Delete plot")
            .setMessage("Are you sure?")
            .setPositiveButton(R.string.ok) { _, _ ->
                viewModel.deletePlot(plot)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }


    private val pointIcon: BitmapDescriptor by lazy {
        getMarkerIcon(R.drawable.ic_point_circle)
    }

    private fun getMarkerIcon(@DrawableRes resId: Int): BitmapDescriptor {
        val drawable = ResourcesCompat.getDrawable(resources, resId, null)!!
        val bitmap = android.graphics.Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            android.graphics.Bitmap.Config.ARGB_8888
        )
        val canvas = android.graphics.Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    companion object {
        private const val PLACEHOLDER_ID = -1L
    }
}
