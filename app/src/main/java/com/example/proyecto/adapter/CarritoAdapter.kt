package com.example.proyecto.adapter

import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.Alimento
import com.example.proyecto.LoginActivity
import com.example.proyecto.R
import com.google.firebase.firestore.FirebaseFirestore

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
       val popMenu = PopupMenu(context, view)
       popMenu.menuInflater.inflate(R.menu.settings_alimento_carrito, popMenu.menu)
       popMenu.show()
       popMenu.setOnMenuItemClickListener { item ->
           val position = carritoList.indexOf(alimento)
           when(item.itemId){
               R.id.nav_eliminar -> {
                   //toast eliminar y nombre del alimento
                   deleteAlimento(position)
                   true
               }
               R.id.nav_editar -> {
                   editAlimento(alimento)
                   true
               }
               else -> false
           }
       }
   }

    private fun deleteAlimento(position: Int) {
        carritoList.removeAt(position)
        notifyItemRemoved(position)
        val db = FirebaseFirestore.getInstance()
        db.collection("carrito").document(LoginActivity.useremail).update("alimentos", carritoList)
        listener.onItemClicked()
    }

    private fun editAlimento(alimento: Alimento) {
        val position = carritoList.indexOf(alimento)
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Editar cantidad")
        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setView(input)
        builder.setPositiveButton("OK") { dialog, which ->
            alimento.cantidad = input.text.toString().toInt()
            carritoList[position] = alimento

            // Actualiza la lista en Firebase
            val db = FirebaseFirestore.getInstance()
            db.collection("carrito").document(LoginActivity.useremail).update("alimentos", carritoList)
            notifyItemChanged(position)
            listener.onItemClicked()

        }
        builder.show()
    }


    private fun comprarFunction(alimento: Alimento, view: View, context: Context) {
        val index = carritoList.indexOf(alimento)
        carritoList.remove(alimento)
        Toast.makeText(context, "Has pulsado en comprar " + alimento.nombre, Toast.LENGTH_SHORT).show()
        buyCarritoList.add(alimento)
        val db = FirebaseFirestore.getInstance()
        db.collection("carrito").document(LoginActivity.useremail).update("alimentos", carritoList)
        db.collection("carrito").document(LoginActivity.useremail).update("alimentosComprados", buyCarritoList)
        notifyItemRemoved(index)
        buyCarritoAdapter?.notifyItemInserted(buyCarritoList.size - 1)
        // Notifica al fragmento sobre el evento de clic
        listener.onItemClicked()


    }


}