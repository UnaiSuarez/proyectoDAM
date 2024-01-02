package com.example.proyecto.utilites

import com.example.proyecto.Alimento
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
        val alimento8 = Alimento("Carne", "proteina")
        val alimento9 = Alimento("Pescado", "proteina")
        val alimento10 = Alimento("Aceite", "grasa")
        val alimento11 = Alimento("Mantequilla", "grasa")
        val alimento12 = Alimento("Nueces", "grasa")
        val alimento13 = Alimento("Fruta", "fruta")
        val alimento14 = Alimento("Verdura", "verdura")
        val alimento15 = Alimento("Legumbre", "legumbre")
        val alimento16 = Alimento("Azucar", "azucar")
        val alimento17 = Alimento("Sal", "sal")
        val alimento18 = Alimento("Chocolate", "dulce")
        val alimento19 = Alimento("Galletas", "dulce")
        val alimento20 = Alimento("Miel", "dulce")
        val alimento21 = Alimento("Pollo", "proteina")
        val alimento22 = Alimento("Pavo", "proteina")
        val alimento23 = Alimento("Cerdo", "proteina")
        val alimento24 = Alimento("Ternera", "proteina")
        val alimento25 = Alimento("Cordero", "proteina")
        val alimento26 = Alimento("Salmón", "proteina")
        val alimento27 = Alimento("Atún", "proteina")
        val alimento28 = Alimento("Trucha", "proteina")
        val alimento29 = Alimento("Merluza", "proteina")
        val alimento30 = Alimento("Sardinas", "proteina")
        val alimento31 = Alimento("Lubina", "proteina")
        val alimento32 = Alimento("Caballa", "proteina")
        val alimento33 = Alimento("Cabracho", "proteina")
        val alimento34 = Alimento("Lenguado", "proteina")
        val alimento35 = Alimento("Bacalao", "proteina")

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
}