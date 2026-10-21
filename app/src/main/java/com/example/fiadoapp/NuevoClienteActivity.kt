package com.example.fiadoapp
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.fiadoapp.ui.theme.FiadoAppTheme
/** Reto extra: una pantalla que devuelve un cliente nuevo a la lista. */
class NuevoClienteActivity : ComponentActivity() {
    companion object {
        const val EXTRA_NOMBRE = "nombre"
        const val EXTRA_TELEFONO = "telefono"
        const val EXTRA_DEUDA = "deuda"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FiadoAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FormularioCliente(
                        onGuardar = { nombre, telefono, deuda ->
                            val respuesta = Intent()
                            respuesta.putExtra(EXTRA_NOMBRE, nombre)
                            respuesta.putExtra(EXTRA_TELEFONO, telefono)
                            respuesta.putExtra(EXTRA_DEUDA, deuda)
                            setResult(RESULT_OK, respuesta)
                            finish()
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
@Composable
fun FormularioCliente(onGuardar: (String, String, Int) -> Unit, modifier: Modifier = Modifier) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var deuda by rememberSaveable { mutableStateOf("") }
    Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "Nuevo cliente",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it.filter { c -> c.isDigit() } },
            label = { Text("Teléfono") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(value = deuda,
            onValueChange = { deuda = it.filter { c -> c.isDigit() } },
            label = { Text("Deuda inicial (pesos)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { onGuardar(nombre.trim(), telefono, deuda.toIntOrNull() ?: 0) },
            enabled = nombre.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) { Text("Guardar") }
    }
}