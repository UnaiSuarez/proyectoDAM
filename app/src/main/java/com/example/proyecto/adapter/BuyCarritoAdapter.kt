package com.example.proyecto.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.Alimento
import com.example.proyecto.R

class BuyCarritoAdapter(
    private val buyCarritoList:ArrayList<Alimento>,
    private val context: Context,
    private val carritoList:ArrayList<Alimento>,
    private val carritoAdapter: CarritoAdapter? = null,
    private val listener: OnItemClickListener):
    RecyclerView.Adapter<BuyCarritoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyCarritoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BuyCarritoViewHolder(layoutInflater.inflate(R.layout.item_carrito_comprado, parent, false))
    }

    override fun getItemCount(): Int = buyCarritoList.size
    override fun onBindViewHolder(holder: BuyCarritoViewHolder, position: Int) {
        val item = buyCarritoList[position]
        holder.nameAlimento.text = item.nombre
        val cantidad = item.cantidad.toString() + "usd"
        holder.cantidad.text = cantidad
        holder.layoutBtn.setOnClickListener {view ->
            devolverFunction(item, view, context)
        }

    }

    private fun callSettings(alimento: Alimento, view: View, context: Context) {

    }

    private fun devolverFunction(alimento: Alimento, view: View, context: Context) {
        val index = buyCarritoList.indexOf(alimento)
        buyCarritoList.remove(alimento)
        Toast.makeText(context, "Has pulsado en devolver " + alimento.nombre, Toast.LENGTH_SHORT).show()
        carritoList.add(alimento)
        notifyItemRemoved(index)
        carritoAdapter?.notifyItemInserted(carritoList.size - 1)
        listener.onItemClicked()
    }
}