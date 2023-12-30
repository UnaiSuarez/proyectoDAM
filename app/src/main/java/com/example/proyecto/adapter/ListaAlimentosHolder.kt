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

    fun render(alimento: Alimento, context: Context) {
        nombre.text = alimento.nombre
        cantidad.text = alimento.cantidad.toString()
        settings.setOnClickListener { view ->
            callSettingsAlimento(alimento, view, context)
        }
    }

    private fun callSettingsAlimento(alimento: Alimento, view: View, context: Context) {
        Toast.makeText(context, "Has pulsado en ${alimento.nombre}", Toast.LENGTH_SHORT).show()
        val popMenu = PopupMenu(context, view)
        popMenu.menuInflater.inflate(R.menu.settings_alimento_menu, popMenu.menu)
        popMenu.show()
        popMenu.setOnMenuItemClickListener { item ->
            when(item.itemId){
                R.id.nav_eliminar -> {
                    //toast eliminar y nombre del alimento
                    Toast.makeText(context, "Has pulsado en eliminar " + nombre.text, Toast.LENGTH_SHORT).show()
                    //deleteAlimento(alimento)
                    true
                }
                R.id.nav_editar -> {
                    Toast.makeText(context, "Has pulsado en editar "+ nombre.text, Toast.LENGTH_SHORT).show()
                    //editAlimento(alimento)
                    true
                }
                else -> false
            }
        }
    }


}