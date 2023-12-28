package com.example.proyecto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.proyecto.LoginActivity.Companion.useremail
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toast.makeText(this, "Bienvenido $useremail", Toast.LENGTH_SHORT).show()
    }

    fun callSignOut(view: View){
        signOut()
    }

    private fun signOut(){
        useremail = ""
        Firebase.auth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
    }

    fun callAddStore(view: View) {}
}