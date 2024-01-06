package com.example.proyecto.adapter

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.R

class BuyCarritoViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    val nameAlimento = view.findViewById<TextView>(R.id.tvAlimentoName)
    val cantidad = view.findViewById<TextView>(R.id.tvCantidadAlimento)
    val settings = view.findViewById<TextView>(R.id.btEditBuyAlimento)
    val layoutBtn = view.findViewById<LinearLayout>(R.id.alimentoComprado)
}