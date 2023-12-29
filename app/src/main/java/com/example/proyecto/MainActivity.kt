package com.example.proyecto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.LoginActivity.Companion.useremail
import com.example.proyecto.adapter.AlmacenAdapter
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var editAlmacen: Almacen
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var alamcenList: ArrayList<Almacen>
    private lateinit var almacenAdapter: AlmacenAdapter

    val manager = GridLayoutManager(this, 2)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toast.makeText(this, "Bienvenido $useremail", Toast.LENGTH_SHORT).show()




        recyclerView = findViewById(R.id.rvAlmacenes)
        recyclerView.layoutManager = manager
        recyclerView.setHasFixedSize(true)

        alamcenList = arrayListOf()
        almacenAdapter = AlmacenAdapter(alamcenList) { almacen: Almacen -> callStore(almacen) }
        recyclerView.adapter = almacenAdapter






    }

    fun callSignOut(view: View){
        signOut()
    }

    private fun signOut(){
        useremail = ""
        editAlmacen = Almacen()
        Firebase.auth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
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
        alamcenList.clear()
    }

    private fun loadRecycler() {
        alamcenList.clear()

        var db = FirebaseFirestore.getInstance()
        db.collection("almacenes").orderBy("nombre")
            .whereEqualTo("dueno", useremail)
            .get()
            .addOnSuccessListener {documents ->
                for (almacen in documents) {
                    val almacenObject = almacen.toObject(Almacen::class.java)
                    val almacenKey = almacen.id
                    almacenObject.key = almacenKey
                    alamcenList.add(almacenObject)

                    almacenAdapter.notifyDataSetChanged()
                }

                recyclerView.layoutManager = manager
                recyclerView.adapter = AlmacenAdapter(alamcenList) { almacen: Almacen ->
                    callStore(
                        almacen
                    )
                }
            }
            .addOnFailureListener{ exception ->
                Toast.makeText(this, "Error al cargar los datos", Toast.LENGTH_SHORT).show()
            }


    }

    fun callStore(almacen: Almacen) {
        editAlmacen = almacen
        Toast.makeText(this, "Has pulsado en ${almacen.nombre}", Toast.LENGTH_SHORT).show()
        val almacenIntent = Intent(this, AlmacenActivity::class.java)
        startActivity(almacenIntent)


    }


   /* private fun addDatos() {


        var db = FirebaseFirestore.getInstance()
        db.collection("aliemntos").document(alimento1.nombre).set(hashMapOf(
            "nombre" to alimento1.nombre,
            "categoria" to alimento1.categoria
        ))
        db.collection("aliemntos").document(alimento2.nombre).set(hashMapOf(
            "nombre" to alimento2.nombre,
            "categoria" to alimento2.categoria
        ))
        db.collection("almacenes").document().set(hashMapOf(
            "dueno" to almacen.dueno,
            "nombre" to almacen.nombre,
            "alimentos" to almacen.alimentos
        ))

        loadRecycler()
        Toast.makeText(this, "Se ha añadido correctamente", Toast.LENGTH_SHORT).show()


    }*/
}