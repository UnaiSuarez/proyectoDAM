package com.example.proyecto.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.Alimento
import com.example.proyecto.Almacen
import com.example.proyecto.R
import com.example.proyecto.fragments.AlmacenFragment

class AlimentosAlmacenAdapter(
    private val alimentosAlmacen: List<Alimento>,
    private val context: Context
) : RecyclerView.Adapter<ListaAlimentosHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaAlimentosHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ListaAlimentosHolder(layoutInflater.inflate(R.layout.item_almacen_alimento, parent, false))
    }

    override fun getItemCount(): Int = alimentosAlmacen.size

    override fun onBindViewHolder(holder: ListaAlimentosHolder, position: Int) {
        val item = alimentosAlmacen[position]
        holder.render(item, context)
    }
}


