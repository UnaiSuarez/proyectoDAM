package com.example.proyecto.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.Alimento
import com.example.proyecto.Almacen
import com.example.proyecto.R

class ListaAlimentosHolder(view: View): RecyclerView.ViewHolder(view) {

    val nombre = view.findViewById<TextView>(R.id.tvAlimentoName)
    val cantidad = view.findViewById<TextView>(R.id.tvCantidadAlimento)
    val settings = view.findViewById<TextView>(R.id.btEditAlimentoAlmacen)





    


}