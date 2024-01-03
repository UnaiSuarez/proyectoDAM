package com.example.proyecto.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.Alimento
import com.example.proyecto.Almacen
import com.example.proyecto.R
import com.example.proyecto.adapter.AlimentosAlmacenAdapter
import com.google.firebase.firestore.FirebaseFirestore

class AlmacenFragment : Fragment(R.layout.fragment_almacen) {


    companion object{
        var alimentosAlmacenList = ArrayList<Alimento>()
        var allAlimentosList = ArrayList<String>()
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var alimentosAlmacenAdapter: AlimentosAlmacenAdapter
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var botonAñadir: ImageButton
    private lateinit var progressBar: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadAllAliemntos()

        progressBar = view.findViewById(R.id.progressBar)

        val tvAlmacenName = view.findViewById<TextView>(R.id.tvAlmacenName)
        tvAlmacenName.setText(MainFragment.editAlmacen.nombre)

        botonAñadir = view.findViewById<ImageButton>(R.id.btAddAlimentoAlmacen)
        botonAñadir.setOnClickListener { view ->
            addAlimento(view)
        }

        autoCompleteTextView = view.findViewById<AutoCompleteTextView>(R.id.etBusquedaAlimento)
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, allAlimentosList)
        autoCompleteTextView.setAdapter(adapter)


        recyclerView = view.findViewById<RecyclerView>(R.id.rvAlimentosAlmacen)
        alimentosAlmacenAdapter = AlimentosAlmacenAdapter(alimentosAlmacenList,requireContext())
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = alimentosAlmacenAdapter
    }


    override fun onResume() {
        super.onResume()
        loadRecycler()
    }

    override fun onPause() {
        super.onPause()
        alimentosAlmacenList.clear()
    }

    fun addAlimento(view: View) {
        addAlimentoPrivate()
    }

    private fun addAlimentoPrivate(){
        var cantidadAlimento = 1
        var alimentoBd: Alimento? = null
        var category: String? = null
        if (autoCompleteTextView.text.toString() != "") {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Editar cantidad")
            val input = EditText(context)
            input.inputType = InputType.TYPE_CLASS_NUMBER
            builder.setView(input)
            builder.setPositiveButton("OK") { dialog, which ->
                if (input.text.toString() != "") {
                    cantidadAlimento = input.text.toString().toInt()
                }else{
                    cantidadAlimento = 1
                }


                val db = FirebaseFirestore.getInstance()
                db.collection("alimentos").document(autoCompleteTextView.text.toString())
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val document = task.result
                            if (document != null && document.exists()) {
                                alimentoBd = document.toObject(Alimento::class.java)
                                category = alimentoBd!!.categoria
                            } else {
                                category = "personalizada"
                            }
                            val alimento: Alimento = Alimento(autoCompleteTextView.text.toString(), category.toString(), cantidadAlimento)
                            alimentosAlmacenList.add(alimento)

                            db.collection("almacenes").document(MainFragment.editAlmacen.key).update("alimentos", alimentosAlmacenList)
                            alimentosAlmacenAdapter.notifyDataSetChanged()
                            autoCompleteTextView.setText("")
                        } else {
                            Toast.makeText(context, "Error al cargar los datos", Toast.LENGTH_SHORT).show()
                        }
                    }



            }
            builder.show()




        } else {
            Toast.makeText(context, "Introduce un alimento", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadRecycler() {
        progressBar.visibility = View.VISIBLE
        MainFragment.almacenList.clear()

        var db = FirebaseFirestore.getInstance()
        db.collection("almacenes").document(MainFragment.editAlmacen.key)
            .get()
            .addOnSuccessListener {documents ->
                val almacen = documents.toObject(Almacen::class.java)
                alimentosAlmacenList = almacen!!.alimentos as ArrayList<Alimento>
                val keyAlmacen = MainFragment.editAlmacen.key

                progressBar.visibility = View.GONE
                alimentosAlmacenAdapter.notifyDataSetChanged()
                alimentosAlmacenAdapter = AlimentosAlmacenAdapter(alimentosAlmacenList, requireContext(), keyAlmacen)
                recyclerView.adapter = alimentosAlmacenAdapter
            }
            .addOnFailureListener{ exception ->
                Toast.makeText(context, "Error al cargar los datos", Toast.LENGTH_SHORT).show()
            }


    }

    private fun loadAllAliemntos() {
        allAlimentosList.clear()
        val db = FirebaseFirestore.getInstance()
        db.collection("alimentos")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val alimento = document.toObject(Alimento::class.java)
                    allAlimentosList.add(alimento.nombre)
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Error al cargar los datos", Toast.LENGTH_SHORT).show()
            }
    }

}