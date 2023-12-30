package com.example.proyecto.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.Alimento
import com.example.proyecto.Almacen
import com.example.proyecto.LoginActivity
import com.example.proyecto.R
import com.example.proyecto.adapter.AlimentosAlmacenAdapter
import com.example.proyecto.adapter.AlmacenAdapter
import com.google.firebase.firestore.FirebaseFirestore

class AlmacenFragment : Fragment(R.layout.fragment_almacen) {


    companion object{
        var alimentosAlmacenList = ArrayList<Alimento>()
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var alimentosAlmacenAdapter: AlimentosAlmacenAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvAlmacenName = view.findViewById<TextView>(R.id.tvAlmacenName)
        tvAlmacenName.setText(MainFragment.editAlmacen.nombre)



        recyclerView = view.findViewById<RecyclerView>(R.id.rvAlimentosAlmacen)
        alimentosAlmacenAdapter = AlimentosAlmacenAdapter(alimentosAlmacenList, requireContext())
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = alimentosAlmacenAdapter
    }

    private fun callSettingsAlimento(alimento: Alimento) {
        Toast.makeText(context, "Has pulsado en ${alimento.nombre}", Toast.LENGTH_SHORT).show()
        val popMenu = PopupMenu(context, view)
        popMenu.menuInflater.inflate(R.menu.settings_alimento_menu, popMenu.menu)
        popMenu.show()
        popMenu.setOnMenuItemClickListener { item ->
            when(item.itemId){
                R.id.nav_eliminar -> {
                    Toast.makeText(context, "Has pulsado en eliminar", Toast.LENGTH_SHORT).show()
                    //deleteAlimento(alimento)
                    true
                }
                R.id.nav_editar -> {
                    Toast.makeText(context, "Has pulsado en editar", Toast.LENGTH_SHORT).show()
                    //editAlimento(alimento)
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadRecycler()
    }

    override fun onPause() {
        super.onPause()
        alimentosAlmacenList.clear()
    }

    private fun loadRecycler() {
        MainFragment.almacenList.clear()

        var db = FirebaseFirestore.getInstance()
        db.collection("almacenes").document(MainFragment.editAlmacen.key)
            .get()
            .addOnSuccessListener {documents ->
                val almacen = documents.toObject(Almacen::class.java)
                alimentosAlmacenList = almacen!!.alimentos as ArrayList<Alimento>


                alimentosAlmacenAdapter.notifyDataSetChanged()
                alimentosAlmacenAdapter = AlimentosAlmacenAdapter(alimentosAlmacenList, requireContext())
                recyclerView.adapter = alimentosAlmacenAdapter
            }
            .addOnFailureListener{ exception ->
                Toast.makeText(context, "Error al cargar los datos", Toast.LENGTH_SHORT).show()
            }


    }

}