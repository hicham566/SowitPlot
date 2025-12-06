package com.example.sowitplot.util

import com.google.android.gms.maps.model.LatLng

object LatLngEncoding {

    fun encode(points: List<LatLng>): String {
        return points.joinToString(";") { "${it.latitude},${it.longitude}" }
    }

    fun decode(encoded: String): List<LatLng> {
        if (encoded.isBlank()) return emptyList()

        return encoded.split(";")
            .filter { it.isNotBlank() }
            .mapNotNull { pair ->
                val parts = pair.split(",")
                if (parts.size != 2) return@mapNotNull null

                val lat = parts[0].toDoubleOrNull()
                val lng = parts[1].toDoubleOrNull()
                if (lat == null || lng == null) null else LatLng(lat, lng)
            }
    }

    fun computeCenter(points: List<LatLng>): LatLng {
        if (points.isEmpty()) return LatLng(0.0, 0.0)

        var latSum = 0.0
        var lngSum = 0.0
        for (p in points) {
            latSum += p.latitude
            lngSum += p.longitude
        }
        val size = points.size.toDouble()
        return LatLng(latSum / size, lngSum / size)
    }
}
