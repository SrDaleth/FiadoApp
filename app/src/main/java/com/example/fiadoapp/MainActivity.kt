package com.example.fiadoapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fiadoapp.ui.theme.FiadoAppTheme

private const val TAG =  "CICLO_MAIN"
class MainActivity : ComponentActivity() {

    private val vm: ClientesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        enableEdgeToEdge()
        setContent {
            FiadoAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PantallaClientes(vm, Modifier.padding(innerPadding))

                }
            }
        }
    }
    override fun onStart() { super.onStart(); Log.d(TAG, "onStart") }
    override fun onResume() { super.onResume(); Log.d(TAG, "onResume") }
    override fun onPause() { super.onPause(); Log.d(TAG, "onPause") }
    override fun onStop() { super.onStop(); Log.d(TAG, "onStop") }
    override fun onRestart() { super.onRestart(); Log.d(TAG, "onRestart") }
    override fun onDestroy() { super.onDestroy(); Log.d(TAG, "onDestroy") }
}



@Composable
fun PantallaClientes(vm: ClientesViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    // Abre DetalleActivity y recibe la nueva deuda cuando esa pantalla termina.
    val abrirDetalle = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { resultado ->
        val datos = resultado.data
        if (resultado.resultCode == Activity.RESULT_OK && datos != null) {
            val id = datos.getIntExtra(DetalleActivity.EXTRA_ID, -1)
            val deuda = datos.getIntExtra(DetalleActivity.EXTRA_DEUDA, 0)
            vm.actualizarDeuda(id, deuda)
        }
    }
    // Reto extra: abre NuevoClienteActivity y recibe el cliente nuevo.
    val abrirNuevo = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { resultado ->
        val datos = resultado.data
        if (resultado.resultCode == Activity.RESULT_OK && datos != null) {
            vm.agregar(
                nombre = datos.getStringExtra(NuevoClienteActivity.EXTRA_NOMBRE) ?: "",
                telefono = datos.getStringExtra(NuevoClienteActivity.EXTRA_TELEFONO) ?: "",
                deuda = datos.getIntExtra(NuevoClienteActivity.EXTRA_DEUDA, 0)
            )
        }
    }
    Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "Fiados de la tienda",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text("Total por cobrar: ${vm.totalPorCobrar} pesos")
        Button(
            onClick = { abrirNuevo.launch(Intent(context, NuevoClienteActivity::class.java)) },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Nuevo cliente") }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(vm.clientes) { cliente ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable {
                        val intent = Intent(context, DetalleActivity::class.java)
                        intent.putExtra(DetalleActivity.EXTRA_ID, cliente.id)
                        intent.putExtra(DetalleActivity.EXTRA_NOMBRE, cliente.nombre)
                        intent.putExtra(DetalleActivity.EXTRA_TELEFONO, cliente.telefono)
                        intent.putExtra(DetalleActivity.EXTRA_DEUDA, cliente.deuda)
                        abrirDetalle.launch(intent)
                    }
                ) {
                    Row(modifier = Modifier.padding(16.dp)) {
                        Text(cliente.nombre, modifier = Modifier.weight(1f))
                        Text("${cliente.deuda} pesos", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
