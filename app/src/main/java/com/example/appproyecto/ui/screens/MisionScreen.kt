package com.example.appproyecto.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appproyecto.ui.theme.AzulBrillante
import com.example.appproyecto.ui.theme.AzulOscuro
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import com.example.appproyecto.ui.theme.Morado
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MisionScreen() {
    val ventana = listOf("Diarias", "Editar", "Crear")
    var ventanaSeleccionada by remember { mutableStateOf(ventana[0]) }

    Box(
        modifier = Modifier
            .background(AzulOscuro)
            .border(4.dp, Color.White)
            .padding(bottom = 14.dp)
            .shadow(elevation = 10.dp, spotColor = AzulBrillante)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ventana.forEachIndexed { index, titulo ->
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = titulo,
                            fontSize = 18.sp,
                            color = if (ventanaSeleccionada == titulo) Color.White else Color.White.copy(
                                alpha = 0.8f
                            ),
                            style = TextStyle(
                                shadow = if (ventanaSeleccionada == titulo)
                                    Shadow(
                                        color = AzulBrillante,
                                        offset = Offset(0f, 0f),
                                        blurRadius = 18f
                                    )
                                else Shadow(Color.Transparent)
                            ),
                            modifier = Modifier
                                .clickable { ventanaSeleccionada = titulo }
                                .padding(horizontal = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(4.dp)
                                .background(
                                    color = if (ventanaSeleccionada == titulo) AzulBrillante else Color.Transparent,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .shadow(
                                    elevation = if (ventanaSeleccionada == titulo) 10.dp else 0.dp,
                                    shape = RoundedCornerShape(10.dp),
                                    spotColor = AzulBrillante
                                )
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.Gray)
            )

            Box(
                modifier = Modifier.padding(vertical = 10.dp)
            ) {
                when (ventanaSeleccionada) {
                    "Diarias" -> PantallaDiarias()
                    "Editar" -> PantallaEditar()
                    "Crear" -> PantallaCrear()
                }
            }
        }
    }
}

@Composable
fun PantallaCrear() {
    val mostrarFormularioMision = remember { mutableStateOf(false) }
    val mostrarFormularioRutina = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(1.dp, Color.White),
            colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box {
                    Text(
                        text = "Crear contenido",
                        color = Color.Blue.copy(alpha = 0.7f),
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(
                            shadow = Shadow(
                                color = AzulBrillante,
                                offset = Offset(0f, 0f),
                                blurRadius = 18f
                            )
                        )
                    )
                    Text(
                        text = "Crear contenido",
                        color = Color.White,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(
                            shadow = Shadow(
                                color = AzulBrillante.copy(alpha = 0.7f),
                                offset = Offset(0f, 0f),
                                blurRadius = 18f
                            )
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(color = Color.Gray)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Misiones creadas",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    items(10) { index ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            shape = RoundedCornerShape(5.dp),
                            border = BorderStroke(1.dp, Color.White),
                            colors = CardDefaults.cardColors(containerColor = Morado.copy(alpha = 0.7f))
                        ) {
                            Text(
                                text = "Misión #$index",
                                color = Color.White,
                                modifier = Modifier.padding(12.dp)
                            )
                        }

                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(color = Color.White)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { mostrarFormularioMision.value = !mostrarFormularioMision.value },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0044FF)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Añadir Misión", color = Color.White)
                }

                if (mostrarFormularioMision.value) {
                    Dialog(onDismissRequest = { mostrarFormularioMision.value = false }) {
                        FormularioCrearMision(
                            onCrear = {
                                mostrarFormularioMision.value = false
                            }
                        )
                    }
                }


                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(color = Color.Gray)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Rutinas creadas",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    items(5) { index ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            shape = RoundedCornerShape(5.dp),
                            border = BorderStroke(1.dp, Color.White),
                            colors = CardDefaults.cardColors(containerColor = Morado.copy(alpha = 0.7f))
                        ) {
                            Text(
                                text = "Rutina #$index",
                                color = Color.White,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(color = Color.White)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { mostrarFormularioRutina.value = !mostrarFormularioRutina.value },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0044FF)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Añadir Rutina", color = Color.White)
                }

                if (mostrarFormularioRutina.value) {
                    FormularioCrearRutina()
                }
            }
        }
    }
}

@Composable
fun FormularioCrearMision(
    onCrear: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .wrapContentHeight()
            .shadow(
                elevation = 30.dp,
                ambientColor = AzulBrillante,
                spotColor = AzulBrillante,
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AzulOscuro),
        border = BorderStroke(1.dp, Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Diseña tu misión",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Nombre de la misión") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Hora (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onCrear,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AAFF)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Crear", color = Color.White)
            }
        }
    }
}


@Composable
fun FormularioCrearRutina() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Nombre de la rutina") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AAFF)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Crear Rutina", color = Color.White)
        }
    }
}


@Composable
fun PantallaEditar() {
    val diasDeLaSemana =
        listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
    val opcionesRutina = listOf("Rutina básica", "Rutina fuerza", "Rutina cardio")
    val rutinasPorDia = remember { mutableStateListOf(*Array(7) { "Rutina básica" }) }
    val expandedStates = remember { mutableStateListOf(*Array(7) { false }) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(1.dp, Color.White),
        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Text(
                    text = "Misiones de la semana",
                    color = Color.Blue.copy(alpha = 0.7f),
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        shadow = Shadow(
                            color = AzulBrillante,
                            offset = Offset(0f, 0f),
                            blurRadius = 18f
                        )
                    )
                )
                Text(
                    text = "Misiones de la semana",
                    color = Color.White,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        shadow = Shadow(
                            color = AzulBrillante.copy(alpha = 0.7f),
                            offset = Offset(0f, 0f),
                            blurRadius = 18f
                        )
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(color = Color.Gray)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box {
                Text(
                    text = "Rutina",
                    color = Color.Blue.copy(alpha = 0.7f),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        shadow = Shadow(
                            color = AzulBrillante,
                            offset = Offset(0f, 0f),
                            blurRadius = 18f
                        )
                    )
                )
                Text(
                    text = "Rutina",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        shadow = Shadow(
                            color = AzulBrillante.copy(alpha = 0.7f),
                            offset = Offset(0f, 0f),
                            blurRadius = 18f
                        )
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            diasDeLaSemana.forEachIndexed { index, dia ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dia,
                        color = Color.White,
                        fontSize = 16.sp
                    )

                    Box {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = rutinasPorDia[index],
                                color = Color.LightGray,
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .clickable { expandedStates[index] = true }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )

                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Info rutina",
                                tint = AzulBrillante,
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable {
                                    }
                            )
                        }

                        DropdownMenu(
                            expanded = expandedStates[index],
                            onDismissRequest = { expandedStates[index] = false }
                        ) {
                            opcionesRutina.forEach { opcion ->
                                DropdownMenuItem(
                                    text = { Text(opcion) },
                                    onClick = {
                                        rutinasPorDia[index] = opcion
                                        expandedStates[index] = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PantallaDiarias() {
    CardMisionDiaria()
}

fun completarMision(
    userId: String,
    fechaHoy: String,
    idMision: String,
    puntos: Long,
    onSuccess: () -> Unit,
    onError: (Exception) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val rutinaRef = db.collection("rutinas").document("${userId}_$fechaHoy")
    val usuarioRef = db.collection("usuarios").document(userId)

    db.runTransaction { transaction ->
        val rutinaSnapshot = transaction.get(rutinaRef)
        val usuarioSnapshot = transaction.get(usuarioRef)

        val misiones = rutinaSnapshot.get("misiones") as? List<Map<String, Any>> ?: throw Exception("Misiones no encontradas")
        val nuevaLista = misiones.map { mision ->
            if (mision["id"] == idMision && mision["completada"] == false) {
                mision.toMutableMap().apply { this["completada"] = true }
            } else mision
        }

        val expActual = (usuarioSnapshot.getLong("exp") ?: 0) + puntos
        val expMax = usuarioSnapshot.getLong("expMax") ?: 100
        var nuevoNivel = usuarioSnapshot.getLong("nivel") ?: 1
        var nuevaExp = expActual

        if (expActual >= expMax) {
            nuevoNivel += 1
            nuevaExp = expActual - expMax
        }

        transaction.update(rutinaRef, "misiones", nuevaLista)
        transaction.update(usuarioRef, mapOf(
            "exp" to nuevaExp,
            "nivel" to nuevoNivel
        ))

        null
    }.addOnSuccessListener {
        onSuccess()
    }.addOnFailureListener {
        onError(it)
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CardMisionDiaria() {

    val misiones = RutinaActual.misiones

    val checks = remember(misiones) {
        mutableStateListOf<MutableState<Boolean>>().apply {
            addAll(misiones.map {
                mutableStateOf(it["completada"] as? Boolean ?: false)
            })
        }
    }

    val checkFinal = checks.all { it.value }

    var tiempoRestante by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        while (true) {
            val ahora = LocalDateTime.now()
            val finDelDia = ahora.toLocalDate().atTime(LocalTime.MIDNIGHT).plusDays(1)
            val duracion = Duration.between(ahora, finDelDia)

            val horas = duracion.toHours()
            val minutos = duracion.toMinutes() % 60
            val segundos = duracion.seconds % 60

            tiempoRestante = String.format("%02d:%02d:%02d", horas, minutos, segundos)
            delay(1000)
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(1.dp, Color.White),
        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Text(
                    text = "Información de la misión",
                    color = Color.Blue.copy(alpha = 0.7f),
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        shadow = Shadow(
                            color = AzulBrillante,
                            offset = Offset(0f, 0f),
                            blurRadius = 18f
                        )
                    )
                )
                Text(
                    text = "Información de la misión",
                    color = Color.White,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        shadow = Shadow(
                            color = AzulBrillante.copy(alpha = 0.7f),
                            offset = Offset(0f, 0f),
                            blurRadius = 18f
                        )
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(color = Color.Gray)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "[Misión Diaria: Llegó el entrenamiento de fuerza.]",
                color = Color.White,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box {
                Text(
                    text = "Objetivo",
                    color = Color.Blue.copy(alpha = 0.7f),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        shadow = Shadow(
                            color = AzulBrillante,
                            offset = Offset(0f, 0f),
                            blurRadius = 18f
                        )
                    )
                )
                Text(
                    text = "Objetivo",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        shadow = Shadow(
                            color = AzulBrillante.copy(alpha = 0.7f),
                            offset = Offset(0f, 0f),
                            blurRadius = 18f
                        )
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            misiones.forEachIndexed { index, mision ->
                val titulo = mision["titulo"] as? String ?: "Sin título"
                val descripcion = mision["descripcion"] as? String ?: ""
                val puntos = mision["puntos"] as? Long ?: 0L

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 0.dp)
                    ) {

                        Text(text = titulo, color = Color.White, fontSize = 15.sp)

                        Text(
                            text = descripcion,
                            color = Color.Gray,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "($puntos puntos)",
                            color = Color.Yellow,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                    }

                    Checkbox(
                        checked = checks[index].value,
                        onCheckedChange = { isChecked ->
                            Log.d("Checkbox", "Cambió a: $isChecked")
                            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@Checkbox
                            val misionId = mision["id"] as? String ?: return@Checkbox
                            val puntos = mision["puntos"] as? Long ?: 0L
                            val fechaHoy = LocalDate.now().toString()
                            val rutinaId = "${userId}_$fechaHoy"
                            val db = FirebaseFirestore.getInstance()

                            val rutinaRef = db.collection("rutinas").document(rutinaId)
                            val usuarioRef = db.collection("usuarios").document(userId)

                            db.runTransaction { transaction ->
                                val rutinaSnapshot = transaction.get(rutinaRef)
                                val usuarioSnapshot = transaction.get(usuarioRef)


                                val misionesActuales = rutinaSnapshot.get("misiones") as? List<Map<String, Any>> ?: return@runTransaction

                                val nuevasMisiones = misionesActuales.map {
                                    if (it["id"] == misionId) {
                                        it.toMutableMap().apply {
                                            this["completada"] = isChecked
                                        }
                                    } else it
                                }

                                transaction.update(rutinaRef, "misiones", nuevasMisiones)

                                val expActual = usuarioSnapshot.getLong("exp") ?: 0
                                val nuevaExp = if (isChecked) expActual + puntos else expActual - puntos
                                transaction.update(usuarioRef, "exp", nuevaExp.coerceAtLeast(0))
                            }.addOnSuccessListener {
                                checks[index].value = isChecked
                            }.addOnFailureListener {
                                Log.e("Mision", "Error al actualizar: ${it.message}")
                            }
                        }
                        ,
                        colors = CheckboxDefaults.colors(
                            checkedColor = AzulBrillante,
                            uncheckedColor = Color.White,
                            checkmarkColor = Color.Black
                        )
                    )
                }
            }


            Spacer(modifier = Modifier.height(26.dp))


            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Checkbox(
                    checked = checkFinal,
                    onCheckedChange = null,
                    modifier = Modifier.size(40.dp),
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.Green,
                        uncheckedColor = Color.Gray,
                        checkmarkColor = Color.Black
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Tiempo restante: $tiempoRestante",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun MisionScreenPreview() {
    MisionScreen()
}