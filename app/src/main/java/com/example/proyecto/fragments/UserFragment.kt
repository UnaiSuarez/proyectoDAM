package com.example.proyecto.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.proyecto.Almacen
import com.example.proyecto.LoginActivity
import com.example.proyecto.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

// Clase UserFragment que hereda de Fragment
class UserFragment : Fragment(R.layout.fragment_user) {

    // Declaración de variables
    private lateinit var lyCuenta: LinearLayout
    private lateinit var tvCuenta: TextView
    private lateinit var tvCerrarSesion: TextView
    private lateinit var tvChangePassword: TextView
    private lateinit var tvDeleteAcount: TextView

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

        tvCerrarSesion = view.findViewById(R.id.tvCerrarSesion)
        tvCerrarSesion.setOnClickListener {
            // Llamada a la función privateCerrarSesion cuando se hace clic en tvCerrarSesion
            signOut()
        }

        tvChangePassword = view.findViewById(R.id.tvChangePassword)
        tvChangePassword.setOnClickListener {
            // Llamada a la función privateChangePassword cuando se hace clic en tvChangePassword
            resetPassword()
        }

        tvDeleteAcount = view.findViewById(R.id.tvDeleteAcount)
        tvDeleteAcount.setOnClickListener {
            // Llamada a la función privateDeleteAcount cuando se hace clic en tvDeleteAcount
            deleteAcount()
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

    private fun signOut(){
        LoginActivity.useremail = ""
        MainFragment.editAlmacen = Almacen()
        Firebase.auth.signOut()
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun resetPassword(){
        FirebaseAuth.getInstance().sendPasswordResetEmail(LoginActivity.useremail)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                   Toast.makeText(context, "Se ha enviado un email para restablecer su contraseña", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Error al enviar el email para restablecer su contraseña", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun deleteAcount(){
       //preguntar si esta seguro de borrar la cuenta
        val builder = android.app.AlertDialog.Builder(context)
        builder.setTitle("Borrar cuenta")
        builder.setMessage("¿Está seguro de que desea borrar su cuenta?")
        builder.setPositiveButton("Aceptar") { dialog, which ->
            //borrar cuenta
            var db = FirebaseFirestore.getInstance()
            db.collection("almacenes").whereEqualTo("dueno", LoginActivity.useremail).get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        db.collection("almacenes").document(document.id).delete()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Error al eliminar el almacén", Toast.LENGTH_LONG).show()
                }
            db.collection("users").document(LoginActivity.useremail).delete()
                .addOnSuccessListener {
                    Toast.makeText(context, "Usuario eliminado correctamente", Toast.LENGTH_LONG).show()
                    signOut()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Error al eliminar el usuario", Toast.LENGTH_LONG).show()
                }
            db.collection("carrito").document(LoginActivity.useremail).delete()
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Error al eliminar el carrito", Toast.LENGTH_LONG).show()
                }
        }
        builder.setNegativeButton("Cancelar", null)
        val dialog: android.app.AlertDialog = builder.create()
        dialog.show()
    }

}
