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
import android.widget.PopupMenu
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


    //companion object para poder acceder a la lista de alimentos desde el adapter
    companion object{
        var alimentosAlmacenList = ArrayList<Alimento>()
        var allAlimentosList = ArrayList<String>()
    }

    //variables
    private lateinit var recyclerView: RecyclerView
    private lateinit var alimentosAlmacenAdapter: AlimentosAlmacenAdapter
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var botonAñadir: ImageButton
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEditAlmacenName: TextView
    private lateinit var tvAlmacenName: TextView

    //onViewCreated se ejecuta cuando la vista se ha creado y se puede acceder a los elementos de la vista
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //cargar los alimentos del almacen
        loadAllAliemntos()

        //inicializar variables
        progressBar = view.findViewById(R.id.progressBar)

        //cargar el nombre del almacen
        tvAlmacenName = view.findViewById<TextView>(R.id.tvAlmacenName)
        //si el nombre del almacen es null, ponerle el nombre por defecto
        tvAlmacenName.setText(MainFragment.editAlmacen.nombre)

        //boton añadir alimento
        botonAñadir = view.findViewById<ImageButton>(R.id.btAddAlimentoAlmacen)
        //al pulsar el boton, añadir el alimento
        botonAñadir.setOnClickListener { view ->
            addAlimento(view)
        }

        //boton editar nombre almacen
        tvEditAlmacenName = view.findViewById(R.id.tvEditAlmacenName)
        //al pulsar el boton, editar el nombre del almacen
        tvEditAlmacenName.setOnClickListener{view ->
            editAlmacenName(view)
        }


        //autocompletar alimento
        autoCompleteTextView = view.findViewById<AutoCompleteTextView>(R.id.etBusquedaAlimento)
        //adaptador para el autocompletar
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, allAlimentosList)
        //asignar el adaptador al autocompletar
        autoCompleteTextView.setAdapter(adapter)


        //recycler view para los alimentos del almacen
        recyclerView = view.findViewById<RecyclerView>(R.id.rvAlimentosAlmacen)
        //adaptador para el recycler view con la lista de alimentos del almacen
        alimentosAlmacenAdapter = AlimentosAlmacenAdapter(alimentosAlmacenList,requireContext())
        //asignar el adaptador al recycler view
        recyclerView.layoutManager = LinearLayoutManager(context)
        //asignar el layout manager al recycler view
        recyclerView.adapter = alimentosAlmacenAdapter
    }


    //onResume se ejecuta cuando la vista se ha creado y se puede acceder a los elementos de la vista
    override fun onResume() {
        super.onResume()
        loadRecycler()
    }

    //onPause se ejecuta cuando la vista se ha creado y se puede acceder a los elementos de la vista
    override fun onPause() {
        super.onPause()
        alimentosAlmacenList.clear()
    }

    //añadir alimento al almacen (funcion publica)
    fun addAlimento(view: View) {
        addAlimentoPrivate()
    }

    //añadir alimento al almacen (funcion privada)
    private fun addAlimentoPrivate(){
        var cantidadAlimento = 1
        var alimentoBd: Alimento? = null
        var category: String? = null
        //si el autocompletar no esta vacio
        if (autoCompleteTextView.text.toString() != "") {
            val builder = AlertDialog.Builder(context)
            //dialogo para introducir la cantidad del alimento
            builder.setTitle("Editar cantidad")
            val input = EditText(context)
            //tipo de entrada de texto numerico
            input.inputType = InputType.TYPE_CLASS_NUMBER
            builder.setView(input)
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
                db.collection("alimentos").document(autoCompleteTextView.text.toString())
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
                            val alimento: Alimento = Alimento(autoCompleteTextView.text.toString(), category.toString(), cantidadAlimento)
                            //añadir el alimento a la lista de alimentos del almacen
                            alimentosAlmacenList.add(alimento)
                            //añadir el alimento a la base de datos
                            db.collection("almacenes").document(MainFragment.editAlmacen.key).update("alimentos", alimentosAlmacenList)
                            //notificar al adaptador que se ha añadido un alimento
                            alimentosAlmacenAdapter.notifyDataSetChanged()
                            //limpiar el autocompletar
                            autoCompleteTextView.setText("")
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

    //cargar el recycler view con los alimentos del almacen
    private fun loadRecycler() {
        //mostrar el progress bar
        progressBar.visibility = View.VISIBLE
        //limpiar la lista de alimentos del almacen
        MainFragment.almacenList.clear()

        var db = FirebaseFirestore.getInstance()
        //cargar los datos del almacen de la base de datos que sea igual al almacen seleccionado
        db.collection("almacenes").document(MainFragment.editAlmacen.key)
            .get()
            .addOnSuccessListener {documents ->
                //convertir los datos a un objeto almacen
                val almacen = documents.toObject(Almacen::class.java)
                //asignar la lista de alimentos del almacen a la lista de alimentos del almacen
                alimentosAlmacenList = almacen!!.alimentos as ArrayList<Alimento>
                //asignar la key del almacen a la key del almacen
                val keyAlmacen = MainFragment.editAlmacen.key
                //ocultar el progress bar
                progressBar.visibility = View.GONE
                //notificar al adaptador que se han añadido alimentos
                alimentosAlmacenAdapter.notifyDataSetChanged()
                //asignar el adaptador al recycler view
                alimentosAlmacenAdapter = AlimentosAlmacenAdapter(alimentosAlmacenList, requireContext(), keyAlmacen)
                //asignar el layout manager al recycler view
                recyclerView.adapter = alimentosAlmacenAdapter
            }
            //si la tarea no se ha completado correctamente
            .addOnFailureListener{ exception ->
                Toast.makeText(context, "Error al cargar los datos", Toast.LENGTH_SHORT).show()
            }


    }

    //cargar todos los alimentos de la base de datos
    private fun loadAllAliemntos() {
        //limpiar la lista de alimentos
        allAlimentosList.clear()
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
                    allAlimentosList.add(alimento.nombre)
                }
            }
            //si la tarea no se ha completado correctamente
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Error al cargar los datos", Toast.LENGTH_SHORT).show()
            }
    }

    //editar el nombre del almacen
    private fun editAlmacenName(view: View){
        //crear el menu emergente
        val popMenu = PopupMenu(context, view)
        //inflar el menu
        popMenu.menuInflater.inflate(R.menu.editalmacen_menu, popMenu.menu)
        //mostrar el menu
        popMenu.show()
        //al pulsar un item del menu
        popMenu.setOnMenuItemClickListener { item ->
            //segun el item pulsado
            when(item.itemId){
                //editar nombre
                R.id.nav_editAlmacen -> {
                    //crear el dialogo para introducir el nombre
                    val builder = AlertDialog.Builder(context)
                    //titulo del dialogo
                    builder.setTitle("Editar nombre")
                    //campo de texto para introducir el nombre
                    val input = EditText(context)
                    //tipo de entrada de texto
                    input.inputType = InputType.TYPE_CLASS_TEXT
                    //asignar el campo de texto al dialogo
                    builder.setView(input)
                    //al pulsar ok, editar el nombre del almacen
                    builder.setPositiveButton("OK") { dialog, which ->
                        //si el campo de texto no esta vacio, editar el nombre del almacen
                        if (input.text.toString() != "") {
                            //asignar el nombre del almacen al campo de texto
                            tvAlmacenName.setText(input.text.toString())
                            //editar el nombre del almacen en la base de datos
                            val db = FirebaseFirestore.getInstance()
                            //editar el nombre del almacen en la base de datos
                            db.collection("almacenes").document(MainFragment.editAlmacen.key).update("nombre", input.text.toString())
                            //editar el nombre del almacen en la lista de almacenes
                            MainFragment.editAlmacen.nombre = input.text.toString()
                        }else{
                            Toast.makeText(context, "Introduce un nombre", Toast.LENGTH_SHORT).show()
                        }
                    }
                    //al pulsar cancelar, cerrar el dialogo
                    builder.show()
                    true
                }
                //eliminar almacen
                R.id.nav_deleteAlmacen -> {
                    val builder = AlertDialog.Builder(context)
                    //titulo del dialogo
                    builder.setTitle("¿Estás seguro de que quieres eliminar el almacén?")
                    builder.setPositiveButton("OK") { dialog, which ->
                        val db = FirebaseFirestore.getInstance()
                        //eliminar el almacen de la base de datos
                        db.collection("almacenes").document(MainFragment.editAlmacen.key).delete()
                        //eliminar el almacen de la lista de almacenes
                        MainFragment.almacenList.remove(MainFragment.editAlmacen)
                        //volver atras
                        val mainFragment = MainFragment()
                        setCurrentFragment(mainFragment)

                    }
                    builder.setNegativeButton("Cancelar") { dialog, which ->
                        //toast cancelar
                    }
                    builder.show()
                    true
                }
                else -> false
            }
        }
    }

    //funcion para cambiar de fragment
    private fun setCurrentFragment(fragment: Fragment) =
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, fragment)
            commit()
        }

}