package com.example.proyecto

import android.os.Parcelable


data class Almacen(
    var alimentos: List<Alimento> = ArrayList(),
    var nombre: String = "",
    var dueno: String = "",
    var key: String = ""
)