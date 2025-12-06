package com.example.sowitplot

import android.view.View
import android.widget.AdapterView
import android.widget.Spinner

inline fun <T> Spinner.setOnItemSelectedListenerCompat(
    crossinline onSelected: (item: T) -> Unit
) {
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            @Suppress("UNCHECKED_CAST")
            val item = parent?.getItemAtPosition(position) as? T ?: return
            onSelected(item)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) = Unit
    }
}
