package com.example.proyecto.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.Alimento
import com.example.proyecto.LoginActivity
import com.example.proyecto.R
import com.example.proyecto.adapter.BuyCarritoAdapter
import com.example.proyecto.adapter.CarritoAdapter
import com.example.proyecto.adapter.OnItemClickListener
import com.google.firebase.firestore.FirebaseFirestore


class CarritoFragment : Fragment(R.layout.fragment_carrito), OnItemClickListener {

    companion object {
        fun newInstance() = CarritoFragment()

        var alimetosCarritoList = ArrayList<Alimento>()
        var alimentosCompradosList = ArrayList<Alimento>()

    }

    private lateinit var lytCarrito: LinearLayout
    private lateinit var lytProductosComprados: LinearLayout
    private lateinit var lytSinAlimentos: LinearLayout
    private lateinit var icFlecha: TextView
    private lateinit var rvCarrito: RecyclerView
    private lateinit var rvComprados: RecyclerView
    private lateinit var carritoAdapter: CarritoAdapter
    private lateinit var buyCarritoAdapter: BuyCarritoAdapter

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


        carritoAdapter = CarritoAdapter(alimetosCarritoList, requireContext(), alimentosCompradosList, listener = this)
        buyCarritoAdapter = BuyCarritoAdapter(alimentosCompradosList, requireContext(), alimetosCarritoList, listener = this)

        // Luego carga los RecyclerViews
        loadRecyclerViewCarrito()
        loadRecyclerViewComprados()



    }

    override fun onResume() {
        super.onResume()
        loadCarrito()
    }




    private fun loadRecyclerViewCarrito(){
        rvCarrito = view?.findViewById(R.id.rvCarrito)!!
        rvCarrito.layoutManager = LinearLayoutManager(context)
        carritoAdapter = CarritoAdapter(alimetosCarritoList, requireContext(), alimentosCompradosList, buyCarritoAdapter ,this)
        rvCarrito.adapter = carritoAdapter

    }

    private fun loadRecyclerViewComprados(){
        rvComprados = view?.findViewById(R.id.rvComprados)!!
        rvComprados.layoutManager = LinearLayoutManager(context)
        buyCarritoAdapter = BuyCarritoAdapter(alimentosCompradosList, requireContext(), alimetosCarritoList, carritoAdapter ,this)
        rvComprados.adapter = buyCarritoAdapter
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
                            loadRecyclerViewCarrito()
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
                            loadRecyclerViewComprados()
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


    override fun onItemClicked() {
        if (alimetosCarritoList.size == 0 && alimentosCompradosList.size == 0){
            lytCarrito.visibility = View.GONE
            lytProductosComprados.visibility = View.GONE
            lytSinAlimentos.visibility = View.VISIBLE
        }
        if (alimetosCarritoList.size == 0 && alimentosCompradosList.size > 0){
            lytCarrito.visibility = View.GONE
            lytProductosComprados.visibility = View.VISIBLE
            lytSinAlimentos.visibility = View.GONE
        }
        if (alimentosCompradosList.size == 0 && alimetosCarritoList.size > 0){
            lytCarrito.visibility = View.VISIBLE
            lytProductosComprados.visibility = View.GONE
            lytSinAlimentos.visibility = View.GONE
        }
        if (alimetosCarritoList.size > 0 && alimentosCompradosList.size > 0){
            lytCarrito.visibility = View.VISIBLE
            lytProductosComprados.visibility = View.VISIBLE
            lytSinAlimentos.visibility = View.GONE
        }
    }


}