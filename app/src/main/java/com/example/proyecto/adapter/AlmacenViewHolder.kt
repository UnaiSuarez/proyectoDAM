package com.example.proyecto.adapter

import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.Almacen
import com.example.proyecto.AlmacenActivity

class AlmacenViewHolder(view: View) : RecyclerView.ViewHolder(view) {



    val nombre = view.findViewById<TextView>(com.example.proyecto.R.id.tvAlmacenName)
    fun render(almacen: Almacen, onClickListener: (Almacen) -> Unit) {
        nombre.text = almacen.nombre
        itemView.setOnClickListener { onClickListener(almacen) }

    }
}