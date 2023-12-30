package com.example.proyecto.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.Alimento
import com.example.proyecto.Almacen
import com.example.proyecto.AlmacenActivity
import com.example.proyecto.LoginActivity
import com.example.proyecto.LoginActivity.Companion.useremail
import com.example.proyecto.R
import com.example.proyecto.adapter.AlmacenAdapter
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class MainFragment : Fragment(R.layout.fragment_main2) {

    companion object {
        lateinit var editAlmacen: Almacen
        val almacenList = ArrayList<Almacen>()

    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var almacenAdapter: AlmacenAdapter

    private val almacenFragment = AlmacenFragment()

    val manager = GridLayoutManager(context, 2)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById<RecyclerView>(R.id.rvAlmacenes)
        almacenAdapter = AlmacenAdapter(almacenList) { almacen: Almacen -> callStore(almacen) }
        recyclerView.layoutManager = manager
        recyclerView.adapter = almacenAdapter


    }


    fun callStore(almacen: Almacen) {
        editAlmacen = almacen
        Toast.makeText(context, "Has pulsado en ${almacen.nombre}", Toast.LENGTH_SHORT).show()
        setCurrentFragment(almacenFragment)


    }




    fun callAddStore(view: View) {}
    fun añadirDatos(view: View) {
        //addDatos()
        loadRecycler()
    }

    override fun onResume() {
        super.onResume()
        loadRecycler()
    }

    override fun onPause() {
        super.onPause()
        almacenList.clear()
    }

    private fun loadRecycler() {
        almacenList.clear()

        var db = FirebaseFirestore.getInstance()
        db.collection("almacenes").orderBy("nombre")
            .whereEqualTo("dueno", useremail)
            .get()
            .addOnSuccessListener {documents ->
                for (almacen in documents) {
                    val almacenObject = almacen.toObject(Almacen::class.java)
                    val almacenKey = almacen.id
                    almacenObject.key = almacenKey
                    almacenList.add(almacenObject)

                    almacenAdapter.notifyDataSetChanged()
                }

                recyclerView.layoutManager = manager
                recyclerView.adapter = AlmacenAdapter(almacenList) { almacen: Almacen ->
                    callStore(
                        almacen
                    )
                }
            }
            .addOnFailureListener{ exception ->
                Toast.makeText(context, "Error al cargar los datos", Toast.LENGTH_SHORT).show()
            }


    }

    private fun setCurrentFragment(fragment: Fragment) =
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, fragment)
            commit()
        }

}