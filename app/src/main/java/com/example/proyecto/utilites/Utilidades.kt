package com.example.proyecto.utilites

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.Alimento
import com.example.proyecto.LoginActivity
import com.example.proyecto.R
import com.example.proyecto.Unidad
import com.example.proyecto.adapter.OnItemClickListener
import com.example.proyecto.fragments.AlmacenFragment
import com.google.firebase.firestore.FirebaseFirestore

class Utilidades {
    companion object {
        fun editAlimento(
            alimento: Alimento,
            lista: MutableList<Alimento>,
            claveCollection: String,
            claveFirebase: String,
            claveDocument: String,
            context: Context,
            listener: OnItemClickListener? = null,
            adapter: RecyclerView.Adapter<*>) {


            val unidades = Unidad.values().map { it.nombreCompleto }
            val position = lista.indexOf(alimento)
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.custom_dialog_layout, null)
            builder.setView(view)
            val input = view.findViewById<TextView>(R.id.textCantidad)
            input.text = alimento.cantidad.toString()
            var cantidad: Int

            val unidad = alimento.unidad

            val botonMas = view.findViewById<TextView>(R.id.button_plus)
            val botonMenos = view.findViewById<TextView>(R.id.button_minus)
            val spinner: Spinner = view.findViewById(R.id.spinnerUnidad)
            val adapterSpinner = ArrayAdapter(context, android.R.layout.simple_spinner_item, unidades)
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapterSpinner
            botonMas.setOnClickListener {
                var cantidad = input.text.toString().toInt()
                cantidad++
                input.text = cantidad.toString()
            }
            botonMenos.setOnClickListener {
                var cantidad = input.text.toString().toInt()
                if (cantidad > 1) {
                    cantidad--
                    input.text = cantidad.toString()
                }
            }
            builder.setPositiveButton("OK") { dialog, which ->
                if (input.text == null || input.text.toString() == "") {
                    cantidad = alimento.cantidad!!
                } else {
                    cantidad = input.text.toString().toInt()
                }
                alimento.cantidad = cantidad
                lista[position] = alimento

                // Actualiza la lista en Firebase
                val db = FirebaseFirestore.getInstance()
                db.collection(claveCollection).document(claveDocument).update(claveFirebase, lista)
                adapter.notifyItemChanged(position)
                listener?.onItemClicked()
            }
            builder.show()
        }
    }
}
