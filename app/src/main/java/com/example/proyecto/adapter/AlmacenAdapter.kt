package com.example.proyecto.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.Almacen
import com.example.proyecto.R

class AlmacenAdapter(private val almacenList:List<Almacen>, private val onClickListener:(Almacen) -> Unit) : RecyclerView.Adapter<AlmacenViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlmacenViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AlmacenViewHolder(layoutInflater.inflate(R.layout.item_almacen, parent, false))
    }

    override fun getItemCount(): Int = almacenList.size
    override fun onBindViewHolder(holder: AlmacenViewHolder, position: Int) {
        val item = almacenList[position]
        holder.render(item, onClickListener)
    }

}