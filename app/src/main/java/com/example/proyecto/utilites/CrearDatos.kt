package com.example.proyecto.utilites

import com.example.proyecto.Alimento
import com.example.proyecto.Carrito
import com.google.firebase.firestore.FirebaseFirestore

class CrearDatos {

    fun crearDatosAlimentos(){
        val alimentos: ArrayList<Alimento> = ArrayList<Alimento>()

        val alimento1 = Alimento("Arroz", "cereal")
        val alimento2 = Alimento("Pasta", "cereal")
        val alimento3 = Alimento("Pan", "cereal")
        val alimento4 = Alimento("Leche", "lacteo")
        val alimento5 = Alimento("Queso", "lacteo")
        val alimento6 = Alimento("Yogur", "lacteo")
        val alimento7 = Alimento("Huevo", "proteina")
        val alimento8 = Alimento("Cordero","carne")
        val alimento9 = Alimento("Cerdo","carne")
        val alimento10 = Alimento("Ternera","carne")
        val alimento11 = Alimento("Pollo","carne")
        val alimento12 = Alimento("Pavo","carne")
        val alimento13 = Alimento("Conejo","carne")
        val alimento14 = Alimento("Salmón","pescado")
        val alimento15 = Alimento("Atún","pescado")
        val alimento16 = Alimento("Merluza","pescado")
        val alimento17 = Alimento("Sardinas","pescado")
        val alimento18 = Alimento("Lenguado","pescado")
        val alimento19 = Alimento("Bacalao","pescado")
        val alimento20 = Alimento("Gambas","pescado")
        val alimento21 = Alimento("Langostinos","pescado")
        val alimento22 = Alimento("Almejas","pescado")
        val alimento23 = Alimento("Mejillones","pescado")
        val alimento24 = Alimento("Calamares","pescado")
        val alimento25 = Alimento("Pulpo","pescado")
        val alimento26 = Alimento("Chocolate","dulce")
        val alimento27 = Alimento("Galletas","dulce")
        val alimento28 = Alimento("Bollería","dulce")
        val alimento29 = Alimento("Helado","dulce")
        val alimento30 = Alimento("Caramelos","dulce")
        val alimento31 = Alimento("Patatas","verdura")
        val alimento32 = Alimento("Tomates","verdura")
        val alimento33 = Alimento("Cebollas","verdura")
        val alimento34 = Alimento("Zanahorias","verdura")
        val alimento35 = Alimento("Pimientos","verdura")

        alimentos.add(alimento1)
        alimentos.add(alimento2)
        alimentos.add(alimento3)
        alimentos.add(alimento4)
        alimentos.add(alimento5)
        alimentos.add(alimento6)
        alimentos.add(alimento7)
        alimentos.add(alimento8)
        alimentos.add(alimento9)
        alimentos.add(alimento10)
        alimentos.add(alimento11)
        alimentos.add(alimento12)
        alimentos.add(alimento13)
        alimentos.add(alimento14)
        alimentos.add(alimento15)
        alimentos.add(alimento16)
        alimentos.add(alimento17)
        alimentos.add(alimento18)
        alimentos.add(alimento19)
        alimentos.add(alimento20)
        alimentos.add(alimento21)
        alimentos.add(alimento22)
        alimentos.add(alimento23)
        alimentos.add(alimento24)
        alimentos.add(alimento25)
        alimentos.add(alimento26)
        alimentos.add(alimento27)
        alimentos.add(alimento28)
        alimentos.add(alimento29)
        alimentos.add(alimento30)
        alimentos.add(alimento31)
        alimentos.add(alimento32)
        alimentos.add(alimento33)
        alimentos.add(alimento34)
        alimentos.add(alimento35)


        var db = FirebaseFirestore.getInstance()
        for (alimento in alimentos){
            db.collection("alimentos").document(alimento.nombre).set(hashMapOf(
                "nombre" to alimento.nombre,
                "categoria" to alimento.categoria
            ))
        }


    }

    fun crearDatosCarrito(){

        val alimentos: ArrayList<Alimento> = ArrayList<Alimento>()
        val alimentosComprados: ArrayList<Alimento> = ArrayList<Alimento>()

        val alimento1 = Alimento("Arroz", "cereal")
        val alimento2 = Alimento("Pasta", "cereal")
        val alimento3 = Alimento("Pan", "cereal")
        val alimento4 = Alimento("Leche", "lacteo")
        val alimento5 = Alimento("Queso", "lacteo")

        alimentos.add(alimento1)
        alimentos.add(alimento2)
        alimentos.add(alimento3)

        alimentosComprados.add(alimento4)
        alimentosComprados.add(alimento5)


        val carrito: Carrito = Carrito(alimentos, alimentosComprados)

        var db = FirebaseFirestore.getInstance()
        db.collection("carrito").document("unai64535@gmail.com").set(hashMapOf(
            "alimentos" to carrito.alimentos,
            "alimentosComprados" to carrito.alimentosComprados
        ))
    }
}