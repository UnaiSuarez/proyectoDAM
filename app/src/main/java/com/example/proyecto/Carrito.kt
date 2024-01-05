package com.example.proyecto

data class Carrito(
    var alimentos: List<Alimento>? = ArrayList(),
    var alimentosComprados: List<Alimento>? = ArrayList(),
)
