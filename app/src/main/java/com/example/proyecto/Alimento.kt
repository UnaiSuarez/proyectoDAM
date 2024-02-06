package com.example.proyecto

data class Alimento(
    var nombre: String = "",
    var categoria: String = "",
    var cantidad: Int? = 0,
    var unidad: Unidad? = null
)
