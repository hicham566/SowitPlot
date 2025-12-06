package com.example.sowitplot

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sowitplot.data.PlotEntity
import java.util.Locale

class PlotsAdapter(
    private val onSelect: (PlotEntity) -> Unit,
    private val onEdit: (PlotEntity) -> Unit,
    private val onDelete: (PlotEntity) -> Unit
) : RecyclerView.Adapter<PlotsAdapter.PlotViewHolder>() {

    private val items = mutableListOf<PlotEntity>()

    fun submitList(list: List<PlotEntity>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class PlotViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image = view.findViewById<ImageView>(R.id.imageThumbnail)
        private val name = view.findViewById<TextView>(R.id.textName)
        private val area = view.findViewById<TextView>(R.id.textArea)
        private val btnEdit = view.findViewById<ImageButton>(R.id.btnEdit)
        private val btnDelete = view.findViewById<ImageButton>(R.id.btnDelete)

        fun bind(plot: PlotEntity) {
            name.text = plot.name
            val ha = plot.areaSqMeters / 10_000.0
            area.text = String.format(Locale.US, "%.2f ha", ha)

            if (!plot.thumbnailPath.isNullOrEmpty()) {
                val bmp = BitmapFactory.decodeFile(plot.thumbnailPath)
                image.setImageBitmap(bmp)
            } else {
                image.setImageResource(R.drawable.ic_point_circle)
            }

            itemView.setOnClickListener { onSelect(plot) }
            btnEdit.setOnClickListener { onEdit(plot) }
            btnDelete.setOnClickListener { onDelete(plot) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlotViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_plot, parent, false)
        return PlotViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlotViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
