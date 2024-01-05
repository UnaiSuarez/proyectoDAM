package com.example.proyecto.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import android.widget.TextView
import com.example.proyecto.Alimento
import com.example.proyecto.LoginActivity
import com.example.proyecto.R
import com.google.firebase.firestore.FirebaseFirestore


class CarritoFragment : Fragment(R.layout.fragment_carrito) {

    companion object {
        var alimetosCarritoList = ArrayList<Alimento>()
        var alimentosCompradosList = ArrayList<Alimento>()
    }

    private lateinit var lytCarrito: LinearLayout
    private lateinit var lytProductosComprados: LinearLayout
    private lateinit var lytSinAlimentos: LinearLayout
    private lateinit var icFlecha: TextView

    val animation = TranslateAnimation(0f, 0f, 0f, 50f)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lytCarrito = view.findViewById(R.id.lytCarrito)
        lytProductosComprados = view.findViewById(R.id.lytProductosComprados)
        lytSinAlimentos = view.findViewById(R.id.lytSinAlimentos)
        icFlecha = view.findViewById(R.id.icFlecha)

        lytCarrito.visibility = View.GONE
        lytProductosComprados.visibility = View.GONE
        lytSinAlimentos.visibility = View.GONE

        animation.duration = 500
        animation.repeatCount = Animation.INFINITE
        animation.repeatMode = Animation.REVERSE
        icFlecha.startAnimation(animation)

        loadCarrito()

    }

    private fun loadCarrito(){

        alimetosCarritoList.clear()
        alimentosCompradosList.clear()

        val db = FirebaseFirestore.getInstance()
        db.collection("carrito").document(LoginActivity.useremail)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val alimentos = document.get("alimentos") as ArrayList<HashMap<String, String>>
                    val alimentosComprados =
                        document.get("alimentosComprados") as ArrayList<HashMap<String, String>>

                    if (alimentos.size > 0 || alimentosComprados.size > 0) {
                        if (alimentos.size > 0) {
                            lytCarrito.visibility = View.VISIBLE
                            lytSinAlimentos.visibility = View.GONE
                            for (alimento in alimentos) {
                                val cantidad: Int = alimento["cantidad"].toString().toInt()
                                if (cantidad > 0) {
                                    val alimento: Alimento = Alimento(
                                        alimento["nombre"].toString(),
                                        alimento["categoria"].toString(),
                                        cantidad
                                    )
                                    alimetosCarritoList.add(alimento)
                                } else {
                                    val alimento: Alimento = Alimento(
                                        alimento["nombre"].toString(),
                                        alimento["categoria"].toString(),
                                        1
                                    )
                                    alimetosCarritoList.add(alimento)
                                }
                            }
                        } else {
                            lytCarrito.visibility = View.GONE
                        }
                        if (alimentosComprados.size > 0) {
                            lytProductosComprados.visibility = View.VISIBLE
                            lytSinAlimentos.visibility = View.GONE
                            for (alimento in alimentosComprados) {
                                val cantidad: Int = alimento["cantidad"].toString().toInt()
                                if (cantidad > 0) {
                                    val alimento: Alimento = Alimento(
                                        alimento["nombre"].toString(),
                                        alimento["categoria"].toString(),
                                        cantidad
                                    )
                                    alimentosCompradosList.add(alimento)
                                } else {
                                    val alimento: Alimento = Alimento(
                                        alimento["nombre"].toString(),
                                        alimento["categoria"].toString(),
                                        1
                                    )
                                    alimentosCompradosList.add(alimento)
                                }
                            }
                        } else {
                            lytProductosComprados.visibility = View.GONE
                        }
                    }

                } else {
                    lytCarrito.visibility = View.GONE
                    lytProductosComprados.visibility = View.GONE
                    lytSinAlimentos.visibility = View.VISIBLE

                }
            }

    }
}