package com.example.proyecto.adapter

import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.Alimento
import com.example.proyecto.Almacen
import com.example.proyecto.R
import com.example.proyecto.fragments.AlmacenFragment
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class AlimentosAlmacenAdapter(
    private val alimentosAlmacen: ArrayList<Alimento>,
    private val context: Context,
    private val keyAlmacen: String? = null
) : RecyclerView.Adapter<ListaAlimentosHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaAlimentosHolder {
        println("----------------------------------------------------------------")
        println("KEY ALMACEN: $keyAlmacen")
        val layoutInflater = LayoutInflater.from(parent.context)
        return ListaAlimentosHolder(layoutInflater.inflate(R.layout.item_almacen_alimento, parent, false))
    }

    override fun getItemCount(): Int = alimentosAlmacen.size

    override fun onBindViewHolder(holder: ListaAlimentosHolder, position: Int) {
        println("onBindViewHolder$alimentosAlmacen")
        val item = alimentosAlmacen[position]
        holder.nombre.text = item.nombre
        holder.cantidad.text = item.cantidad.toString()
        holder.settings.setOnClickListener { view ->
            callSettingsAlimento(item, view, context)
        }
    }

    private fun callSettingsAlimento(alimento: Alimento, view: View, context: Context) {
        Toast.makeText(context, "Has pulsado en ${alimento.nombre}", Toast.LENGTH_SHORT).show()
        val popMenu = PopupMenu(context, view)
        popMenu.menuInflater.inflate(R.menu.settings_alimento_menu, popMenu.menu)
        popMenu.show()
        popMenu.setOnMenuItemClickListener { item ->
            val position = alimentosAlmacen.indexOf(alimento)
            when(item.itemId){
                R.id.nav_eliminar -> {
                    //toast eliminar y nombre del alimento
                    Toast.makeText(context, "Has pulsado en eliminar " + alimento.nombre, Toast.LENGTH_SHORT).show()
                    deleteAlimento(position)
                    true
                }
                R.id.nav_editar -> {
                    Toast.makeText(context, "Has pulsado en editar "+ alimento.nombre, Toast.LENGTH_SHORT).show()
                    editAlimento(alimento)
                    true
                }
                else -> false
            }
        }
    }

    private fun deleteAlimento(position: Int) {

        println("antes de eliminar $alimentosAlmacen $position")
        alimentosAlmacen.removeAt(position)
        println("despues de eliminar $alimentosAlmacen")

        // Actualiza la lista en Firebase
        val db = FirebaseFirestore.getInstance()
        db.collection("almacenes").document(keyAlmacen!!).update("alimentos", alimentosAlmacen)

        notifyItemRemoved(position)
    }

    private fun editAlimento(alimento: Alimento) {
        val position = alimentosAlmacen.indexOf(alimento)
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Editar cantidad")
        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setView(input)
        builder.setPositiveButton("OK") { dialog, which ->
            alimento.cantidad = input.text.toString().toInt()
            alimentosAlmacen[position] = alimento

            // Actualiza la lista en Firebase
            val db = FirebaseFirestore.getInstance()
            db.collection("almacenes").document(keyAlmacen!!).update("alimentos", alimentosAlmacen)
            notifyItemChanged(position)

        }
        builder.show()
    }

}


