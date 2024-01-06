package com.example.proyecto.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.Alimento
import com.example.proyecto.R

class CarritoAdapter(
    private val carritoList:ArrayList<Alimento>,
    private val context: Context,
    private val buyCarritoList:ArrayList<Alimento>,
    private val buyCarritoAdapter: BuyCarritoAdapter? = null,
    private val listener: OnItemClickListener):
    RecyclerView.Adapter<CarritoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CarritoViewHolder(layoutInflater.inflate(R.layout.item_carrito, parent, false))
    }

    override fun getItemCount(): Int = carritoList.size
    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val item = carritoList[position]
        holder.nameAlimento.text = item.nombre
        val cantidad = item.cantidad.toString() + "usd"
        holder.cantidad.text = cantidad
        holder.alimentoCarrito.setOnClickListener {view ->
            comprarFunction(item, view, context)
        }
        holder.settings.setOnClickListener {view ->
            callSettings(item, view, context)
        }
    }

   private fun callSettings(alimento: Alimento, view: View, context: Context) {
       Toast.makeText(context, "Has pulsado en settings " + alimento.nombre, Toast.LENGTH_SHORT).show()
   }
    private fun comprarFunction(alimento: Alimento, view: View, context: Context) {
        val index = carritoList.indexOf(alimento)
        carritoList.remove(alimento)
        Toast.makeText(context, "Has pulsado en comprar " + alimento.nombre, Toast.LENGTH_SHORT).show()
        buyCarritoList.add(alimento)
        notifyItemRemoved(index)
        buyCarritoAdapter?.notifyItemInserted(buyCarritoList.size - 1)
        // Notifica al fragmento sobre el evento de clic
        listener.onItemClicked()
    }


}