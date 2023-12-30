package com.example.proyecto

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.example.proyecto.adapter.AlmacenViewHolder

class AlmacenActivity : AppCompatActivity() {

    private lateinit var tvMensaje: TextView



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_almacen)


        tvMensaje = findViewById(R.id.tvAlmacenName)
        //tvMensaje.text = MainActivity.editAlmacen.toString()

    }


}