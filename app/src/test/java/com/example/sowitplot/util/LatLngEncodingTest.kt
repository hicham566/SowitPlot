package com.example.sowitplot.util

import com.example.sowitplot.util.LatLngEncoding
import com.google.android.gms.maps.model.LatLng
import org.junit.Test
import kotlin.test.assertEquals

class LatLngEncodingTest {

    @Test
    fun encodeDecode_roundTrip() {
        val original = listOf(
            LatLng(33.0, -7.0),
            LatLng(33.5, -7.5),
            LatLng(34.0, -8.0)
        )

        val encoded = LatLngEncoding.encode(original)
        val decoded = LatLngEncoding.decode(encoded)

        assertEquals(original.size, decoded.size)
        original.indices.forEach { i ->
            assertEquals(original[i].latitude, decoded[i].latitude, 1e-6)
            assertEquals(original[i].longitude, decoded[i].longitude, 1e-6)
        }
    }

    @Test
    fun computeCenter_returnsAveragePoint() {
        val points = listOf(
            LatLng(0.0, 0.0),
            LatLng(2.0, 2.0)
        )

        val center = LatLngEncoding.computeCenter(points)

        assertEquals(1.0, center.latitude, 1e-6)
        assertEquals(1.0, center.longitude, 1e-6)
    }
}
