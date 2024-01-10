package com.example.proyecto.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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
        var alimetosCarritoList = ArrayList<Alimento>()
        var alimentosCompradosList = ArrayList<Alimento>()
        var allAlimentosList = ArrayList<String>()
    }

    private lateinit var lytCarrito: LinearLayout
    private lateinit var lytProductosComprados: LinearLayout
    private lateinit var lytSinAlimentos: LinearLayout
    private lateinit var icFlecha: TextView
    private lateinit var rvCarrito: RecyclerView
    private lateinit var rvComprados: RecyclerView
    private lateinit var carritoAdapter: CarritoAdapter
    private lateinit var buyCarritoAdapter: BuyCarritoAdapter
    private lateinit var etBusquedaAlimento: AutoCompleteTextView
    private lateinit var botonAñadir: ImageButton

    val animation = TranslateAnimation(0f, 0f, 0f, 50f)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadAllAliemntos()

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

        //autocompletar alimento
        etBusquedaAlimento = view.findViewById<AutoCompleteTextView>(R.id.etBusquedaAlimento)
        //adaptador para el autocompletar
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, AlmacenFragment.allAlimentosList)
        //asignar el adaptador al autocompletar
        etBusquedaAlimento.setAdapter(adapter)


        carritoAdapter = CarritoAdapter(alimetosCarritoList, requireContext(), alimentosCompradosList, listener = this)
        buyCarritoAdapter = BuyCarritoAdapter(alimentosCompradosList, requireContext(), alimetosCarritoList, listener = this)

        // Luego carga los RecyclerViews
        loadRecyclerViewCarrito()
        loadRecyclerViewComprados()

        botonAñadir = view.findViewById<ImageButton>(R.id.btAddAlimentoAlmacen)
        //al pulsar el boton, añadir el alimento
        botonAñadir.setOnClickListener { view ->
            addAlimento()
        }



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

    private fun loadAllAliemntos() {
        //limpiar la lista de alimentos
        AlmacenFragment.allAlimentosList.clear()
        val db = FirebaseFirestore.getInstance()
        //cargar todos los alimentos de la base de datos
        db.collection("alimentos")
            .get()
            .addOnSuccessListener { documents ->
                //recorrer los documentos y añadir los alimentos a la lista de alimentos
                for (document in documents) {
                    //convertir los datos a un objeto alimento
                    val alimento = document.toObject(Alimento::class.java)
                    //añadir el nombre del alimento a la lista de alimentos
                    AlmacenFragment.allAlimentosList.add(alimento.nombre)
                }
            }
            //si la tarea no se ha completado correctamente
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Error al cargar los datos", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addAlimento(){
        var cantidadAlimento = 1
        var alimentoBd: Alimento? = null
        var category: String? = null
        //si el autocompletar no esta vacio
        if (etBusquedaAlimento.text.toString() != "") {
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.custom_dialog_layout, null)
            builder.setView(view)
            val input = view.findViewById<TextView>(R.id.textCantidad)
            input.text = cantidadAlimento.toString()
            var cantidad: Int
            val botonMas = view.findViewById<TextView>(R.id.button_plus)
            val botonMenos = view.findViewById<TextView>(R.id.button_minus)
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
            //al pulsar ok, añadir el alimento al almacen
            builder.setPositiveButton("OK") { dialog, which ->
                //si la cantidad no esta vacia, asignar la cantidad introducida, si esta vacia, asignar 1
                if (input.text.toString() != "") {
                    //convertir el texto a entero
                    cantidadAlimento = input.text.toString().toInt()
                }
                //si la cantidad es menor que 1, asignar 1
                else{
                    cantidadAlimento = 1
                }

                //si el alimento esta en la base de datos, asignar la categoria del alimento, si no, asignar personalizada
                val db = FirebaseFirestore.getInstance()
                //comprobar si el alimento esta en la base de datos
                db.collection("alimentos").document(etBusquedaAlimento.text.toString())
                    .get()
                    .addOnCompleteListener { task ->
                        //si la tarea se ha completado correctamente
                        if (task.isSuccessful) {
                            val document = task.result
                            //si el documento existe, asignar la categoria del alimento, si no, asignar personalizada
                            if (document != null && document.exists()) {
                                alimentoBd = document.toObject(Alimento::class.java)
                                category = alimentoBd!!.categoria
                            } else {
                                category = "personalizada"
                            }
                            //crear el alimento
                            val alimento: Alimento = Alimento(etBusquedaAlimento.text.toString(), category.toString(), cantidadAlimento)
                            //añadir el alimento a la lista de alimentos del almacen
                            alimetosCarritoList.add(alimento)
                            //añadir el alimento a la base de datos
                            db.collection("carrito").document(LoginActivity.useremail).update("alimentos",
                                alimetosCarritoList
                            )
                            //notificar al adaptador que se ha añadido un alimento
                            loadRecyclerViewCarrito()
                            onItemClicked()
                            //limpiar el autocompletar
                            etBusquedaAlimento.setText("")
                        }
                        //si la tarea no se ha completado correctamente
                        else {
                            //toast error
                            Toast.makeText(context, "Error al cargar los datos", Toast.LENGTH_SHORT).show()
                        }
                    }



            }
            //al pulsar cancelar, cerrar el dialogo
            builder.show()
        }
        //si el autocompletar esta vacio, mostrar toast
        else {
            Toast.makeText(context, "Introduce un alimento", Toast.LENGTH_SHORT).show()
        }
    }

}