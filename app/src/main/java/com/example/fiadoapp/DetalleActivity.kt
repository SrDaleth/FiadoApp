package com.example.fiadoapp
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fiadoapp.ui.theme.FiadoAppTheme
private const val TAG = "CICLO_DETALLE"
class DetalleActivity : ComponentActivity() {
    companion object {
        const val EXTRA_ID = "id"
        const val EXTRA_NOMBRE = "nombre"
        const val EXTRA_TELEFONO = "telefono"
        const val EXTRA_DEUDA = "deuda"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        enableEdgeToEdge()
        // Los datos llegan en el Intent que abrió esta pantalla.
        val id = intent.getIntExtra(EXTRA_ID, -1)
        val nombre = intent.getStringExtra(EXTRA_NOMBRE) ?: "Sin nombre"
        val telefono = intent.getStringExtra(EXTRA_TELEFONO) ?: ""
        val deudaInicial = intent.getIntExtra(EXTRA_DEUDA, 0)
        setContent {
            FiadoAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PantallaDetalle(
                        nombre = nombre,
                        telefono = telefono,
                        deudaInicial = deudaInicial,
                        onGuardar = { nuevaDeuda ->
                            // La respuesta viaja de vuelta en otro Intent.
                            val respuesta = Intent()
                            respuesta.putExtra(EXTRA_ID, id)
                            respuesta.putExtra(EXTRA_DEUDA, nuevaDeuda)
                            setResult(RESULT_OK, respuesta)
                            finish()
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
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
fun PantallaDetalle(
    nombre: String,
    telefono: String,
    deudaInicial: Int,
    onGuardar: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var deuda by rememberSaveable { mutableStateOf(deudaInicial) }
    Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(nombre, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Teléfono: $telefono")
        Text("Debe: $deuda pesos", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Button(
            onClick = { deuda = (deuda - 5000).coerceAtLeast(0) },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Registrar abono de 5.000") }
        // Intents implícitos: Android busca qué app puede hacer cada cosa.
        OutlinedButton(
            onClick = {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$telefono"))
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Llamar") }
        OutlinedButton( onClick = {
            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$telefono"))
            val mensaje = "Hola $nombre, te recordamos que tu fiado es de $deuda pesos."
            intent.putExtra("sms_body", mensaje)
            context.startActivity(intent)
        },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Enviar recordatorio por SMS") }
        OutlinedButton(
            onClick = {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, "Fiado de $nombre: $deuda pesos")
                context.startActivity(Intent.createChooser(intent, "Compartir con"))
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Compartir") }
        Button(onClick = { onGuardar(deuda) }, modifier = Modifier.fillMaxWidth()) {
            Text("Guardar y volver")
        }
    }
}
