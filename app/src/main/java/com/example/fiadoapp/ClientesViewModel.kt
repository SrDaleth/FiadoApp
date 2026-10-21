package com.example.fiadoapp

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

/** Guarda la lista de clientes. Sobrevive a que el celular gire. */

class ClientesViewModel : ViewModel() {
    val clientes =  mutableStateListOf(
        Cliente(1, "Doña Marta", "3001234567", 25000),
        Cliente(2, "Don Pedro", "3109876543", 18000),
        Cliente(1, "Luisa", "3205551234", 42000),
        Cliente(1, "Andrés", "3157778899", 0),
    )

    val totalPorCobrar: Int
        get() =  clientes.sumOf {it.deuda}
    fun actualizarDeuda(id: Int, nuevaDeuda: Int) {
        val posicion = clientes.indexOfFirst { it.id == id }
        if (posicion >= 0) clientes[posicion] = clientes[posicion].copy(deuda = nuevaDeuda)
    }

    fun agregar(nombre: String, telefono: String, deuda: Int) {
        val nuevoId = (clientes.maxOfOrNull { it.id } ?: 0) + 1
        clientes.add(Cliente(nuevoId, nombre, telefono, deuda))
    }
}