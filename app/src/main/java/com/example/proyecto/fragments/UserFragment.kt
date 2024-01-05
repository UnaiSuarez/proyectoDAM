package com.example.proyecto.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.proyecto.R

// Clase UserFragment que hereda de Fragment
class UserFragment : Fragment(R.layout.fragment_user) {

    // Declaración de variables
    private lateinit var lyCuenta: LinearLayout
    private lateinit var tvCuenta: TextView

    // Método que se ejecuta cuando la vista del fragmento ha sido creada
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicialización de las variables
        lyCuenta = view.findViewById(R.id.lyCuenta)
        lyCuenta.visibility = View.GONE

        tvCuenta = view.findViewById(R.id.tvCuenta)
        tvCuenta.setOnClickListener {
            // Llamada a la función privateOpenSettingsCuenta cuando se hace clic en tvCuenta
            privateOpenSettingsCuenta()
        }
    }

    // Función que cambia la visibilidad de lyCuenta
    private fun privateOpenSettingsCuenta() {
        if (lyCuenta.visibility == View.GONE) {
            lyCuenta.visibility = View.VISIBLE
        } else {
            lyCuenta.visibility = View.GONE
        }
    }
}
