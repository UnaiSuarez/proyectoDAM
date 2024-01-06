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
        val popMenu = PopupMenu(context, view)
        popMenu.menuInflater.inflate(R.menu.settings_alimento_menu, popMenu.menu)
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
        buyCarritoList.removeAt(position)
        notifyItemRemoved(position)
        val db = FirebaseFirestore.getInstance()
        db.collection("carrito").document(LoginActivity.useremail).update("alimentosComprados", buyCarritoList)
        listener.onItemClicked()
    }

    private fun editAlimento(alimento: Alimento) {
        val position = buyCarritoList.indexOf(alimento)
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Editar cantidad")
        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setView(input)
        builder.setPositiveButton("OK") { dialog, which ->
            alimento.cantidad = input.text.toString().toInt()
            buyCarritoList[position] = alimento

            // Actualiza la lista en Firebase
            val db = FirebaseFirestore.getInstance()
            db.collection("carrito").document(LoginActivity.useremail).update("alimentosComprados", buyCarritoList)
            notifyItemChanged(position)
            listener.onItemClicked()

        }
        builder.show()
    }

    private fun devolverFunction(alimento: Alimento, view: View, context: Context) {
        val index = buyCarritoList.indexOf(alimento)
        buyCarritoList.remove(alimento)
        Toast.makeText(context, "Has pulsado en devolver " + alimento.nombre, Toast.LENGTH_SHORT).show()
        carritoList.add(alimento)
        notifyItemRemoved(index)
        carritoAdapter?.notifyItemInserted(carritoList.size - 1)
        listener.onItemClicked()
        val db = FirebaseFirestore.getInstance()
        db.collection("carrito").document(LoginActivity.useremail).update("alimentos", carritoList)
        db.collection("carrito").document(LoginActivity.useremail).update("alimentosComprados", buyCarritoList)
    }
}