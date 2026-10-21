package com.example.fiadoapp

/** Un cliente de la tienda y lo que debe**/

data class Cliente (
    val id: Int,
    val nombre: String,
    val telefono: String,
    val deuda: Int
)