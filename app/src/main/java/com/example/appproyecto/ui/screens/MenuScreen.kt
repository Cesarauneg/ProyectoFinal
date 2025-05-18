package com.example.appproyecto.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.appproyecto.R
import com.example.appproyecto.ui.navigation.Screen
import com.example.appproyecto.ui.theme.AzulBrillante
import com.example.appproyecto.ui.theme.AzulOscuro
import com.example.appproyecto.ui.theme.Morado
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt
import java.time.DayOfWeek

sealed class MenuInterna(val ruta: String) {
    object Principal : MenuInterna("menu_principal")
    object MisionesDiarias : MenuInterna("misiones/Diarias")
    object MisionesCrear : MenuInterna("misiones/Crear")
    object MisionesEditar : MenuInterna("misiones/Editar")
    object Perfil : MenuInterna("perfil")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MenuScreen(navController: NavController) {
    val internalNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(internalNavController)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .background(Morado)
        ) {
            NavHost(
                navController = internalNavController,
                startDestination = MenuInterna.Principal.ruta
            ) {
                composable(MenuInterna.Principal.ruta) {
                    MenuScreenContent(
                        navController = internalNavController
                    )
                }

                composable(
                    "misiones/{tab}",
                    arguments = listOf(navArgument("tab") { defaultValue = "Diarias" })
                ) { backStackEntry ->
                    val tab = backStackEntry.arguments?.getString("tab") ?: "Diarias"
                    MisionScreen(tab)
                }

                composable(MenuInterna.Perfil.ruta) {
                    PerfilScreen(navController)
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Inicio", MenuInterna.Principal.ruta, Icons.Default.Home),
        BottomNavItem("Misiones", MenuInterna.MisionesDiarias.ruta, Icons.Default.Checklist),
        BottomNavItem("Perfil", MenuInterna.Perfil.ruta, Icons.Default.Person),
    )

    NavigationBar(
        containerColor = Color.DarkGray,
        tonalElevation = 8.dp,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route?.substringBefore("/")

        items.forEach { item ->
            val selected = currentRoute == item.route.substringBefore("/")
            NavigationBarItem(
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.title,
                        tint = if (selected) AzulBrillante else Color.LightGray,
                        modifier = Modifier.size(if (selected) 28.dp else 24.dp)
                    )
                },
                label = {
                    Text(
                        item.title,
                        color = if (selected) AzulBrillante else Color.Gray
                    )
                },
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(MenuInterna.Principal.ruta) {
                            inclusive = false
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
                ,
                alwaysShowLabel = true
            )
        }
    }
}


data class BottomNavItem(
    val title: String,
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

suspend fun obtenerDatosUsuario(): Map<String, Any> {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return emptyMap()
    val usuarioSnapshot = FirebaseFirestore.getInstance()
        .collection("usuarios")
        .document(userId)
        .get()
        .await()

    return mapOf(
        "nombre" to (usuarioSnapshot.getString("nombre") ?: "Usuario"),
        "nivel" to (usuarioSnapshot.getLong("nivel") ?: 1),
        "exp" to (usuarioSnapshot.getLong("exp") ?: 0),
        "expMax" to (usuarioSnapshot.getLong("expMax") ?: 100),
        "racha" to (usuarioSnapshot.getLong("racha") ?: 0)
    )
}

fun misionDestacada(misionesDelDia: List<Map<String, Any>>): Map<String, Any> {
    val misionMayorPuntaje = misionesDelDia.maxByOrNull { (it["puntos"] as? Long) ?: 0L }

    val id = misionMayorPuntaje?.get("id") as? String ?: "Sin id"
    val titulo = misionMayorPuntaje?.get("titulo") as? String ?: "Sin título"
    val descripcion = misionMayorPuntaje?.get("descripcion") as? String ?: "Sin descripción"
    val puntos = misionMayorPuntaje?.get("puntos") as? String ?: 0
    val completadaMision = misionMayorPuntaje?.get("completada") as? Boolean ?: false

    return mapOf(
        "id" to id,
        "titulo" to titulo,
        "descripcion" to descripcion,
        "puntos" to puntos,
        "completada" to completadaMision
    )
}

@RequiresApi(Build.VERSION_CODES.O)
suspend fun recalcularRachaDesdeRutinas(): Long {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return 0L
    val db = FirebaseFirestore.getInstance()
    val usuarioRef = db.collection("usuarios").document(userId)

    val hoy = LocalDate.now()
    val diasARetroceder = 15

    var racha: Long = 0
    var ultimoDiaConMision: LocalDate? = null

    for (i in 0 until diasARetroceder) {
        val dia = hoy.minusDays(i.toLong())
        val docId = "${userId}_$dia"
        val snap = db.collection("rutinas").document(docId).get().await()

        if (snap.exists()) {
            val misiones = snap.get("misiones") as? List<Map<String, Any>> ?: emptyList()
            val completadas = misiones.count { it["completada"] == true }

            if (completadas > 0) {
                if (ultimoDiaConMision == null) {
                    ultimoDiaConMision = dia // Guardamos el último día en que completó algo
                }
                racha++
            } else {
                if (i != 0) break // Solo seguimos contando si no es hoy
            }
        } else {
            if (i != 0) break
        }
    }

    val updates = mutableMapOf<String, Any>(
        "racha" to racha
    )

    if (ultimoDiaConMision != null) {
        updates["ultimoDiaActivo"] = ultimoDiaConMision.toString()
    } else {
        updates["ultimoDiaActivo"] = FieldValue.delete()
    }

    usuarioRef.update(updates).await()

    return racha
}

fun calcularNivelYExperiencia(
    expActual: Long,
    nivelActual: Long,
    puntos: Long,
): Triple<Long, Long, Long> {
    var nuevaExp = expActual + puntos
    var nuevoNivel = nivelActual
    var nuevaExpMax = 100 + (nuevoNivel - 1) * 20

    while (nuevaExp >= nuevaExpMax) {
        nuevaExp -= nuevaExpMax
        nuevoNivel += 1
        nuevaExpMax = 100 + (nuevoNivel - 1) * 20
    }

    while (nuevaExp < 0 && nuevoNivel > 1) {
        nuevoNivel -= 1
        nuevaExpMax = 100 + (nuevoNivel - 1) * 20
        nuevaExp += nuevaExpMax
    }

    nuevaExp = nuevaExp.coerceAtLeast(0)

    return Triple(nuevaExp, nuevoNivel, nuevaExpMax)
}

@RequiresApi(Build.VERSION_CODES.O)
suspend fun obtenerProgresoSemanal(): List<Int> {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return emptyList()
    val db = FirebaseFirestore.getInstance()

    val hoy = LocalDate.now()
    val primerDiaSemana = hoy.with(DayOfWeek.MONDAY)

    val progreso = mutableListOf<Int>()

    for (i in 0..6) {
        val dia = primerDiaSemana.plusDays(i.toLong())
        val docId = "${userId}_$dia"
        val snap = db.collection("rutinas").document(docId).get().await()

        val completadas = if (snap.exists()) {
            val misiones = snap.get("misiones") as? List<Map<String, Any>> ?: emptyList()
            misiones.count { it["completada"] == true }
        } else 0

        progreso.add(completadas)
    }

    return progreso
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MenuScreenContent(navController: NavController) {
    val progresoSemana = remember { mutableStateOf<List<Int>>(listOf(0, 0, 0, 0, 0, 0, 0)) }
    val misionesState = remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    val nombreUsuario = remember { mutableStateOf("Cargando...") }
    val nivelUsuario = remember { mutableStateOf(0L) }
    val expUsuario = remember { mutableStateOf(0L) }
    val expMaxUsuario = remember { mutableStateOf(100L) }
    val rachaUsuario = remember { mutableStateOf(0L) }
    val fraseDelDia = remember { mutableStateOf("Cargando frase...") }

    LaunchedEffect(Unit) {
        recalcularRachaDesdeRutinas()

        val misiones = obtenerRutinaDelDia()
        val usuario = obtenerDatosUsuario()

        Log.d("MenuScreen", "Usuario datos: $usuario")

        misionesState.value = misiones
        nombreUsuario.value = usuario["nombre"] as String
        nivelUsuario.value = usuario["nivel"] as Long
        expUsuario.value = usuario["exp"] as Long
        expMaxUsuario.value = usuario["expMax"] as Long
        rachaUsuario.value = usuario["racha"] as? Long ?: 0L

        progresoSemana.value = obtenerProgresoSemanal()

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val fechaHoy = LocalDate.now().toString()

        if (userId != null) {
            val rutinaId = "${userId}_$fechaHoy"
            val db = FirebaseFirestore.getInstance()

            val doc = db.collection("rutinas").document(rutinaId).get().await()
            val frase = doc.getString("fraseDelDia") ?: "Sigue dando lo mejor de ti."
            fraseDelDia.value = frase
        }
    }

    val misionesDelDia = misionesState.value
    val misionMayorPuntaje = misionDestacada(misionesDelDia)

    val misionId = misionMayorPuntaje["id"] as? String ?: return
    val titulo = misionMayorPuntaje["titulo"] as? String ?: "Sin título"
    val descripcion = misionMayorPuntaje["descripcion"] as? String ?: "Sin descripción"
    val completadaMisionActualizada = misionesDelDia
        .find { it["id"] == misionId }
        ?.get("completada") as? Boolean ?: false


    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .background(AzulOscuro)
            .border(width = 4.dp, color = Color.White)
            .padding(bottom = 14.dp)
            .shadow(
                elevation = 10.dp,
                spotColor = AzulBrillante
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painterResource(R.drawable.ic_launcher_foreground),
                    "Mi Icono",
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = nombreUsuario.value,
                    fontSize = 15.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.width(20.dp))

                Text("🔥", fontSize = 15.sp)
                Spacer(modifier = Modifier.width(5.dp))
                Text("${rachaUsuario.value}", fontSize = 15.sp, color = Color.White)
            }


            BarraDeExperiencia(
                nivelActual = nivelUsuario.value.toInt(),
                experienciaActual = expUsuario.value.toInt(),
                experienciaNecesaria = expMaxUsuario.value.toInt()
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(color = Color.Gray)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "🌟 Misión Destacada",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 15.sp,
                        color = Color.White
                    )
                )
            }

            if (misionesDelDia.isNotEmpty()) {
                MisionDestacadaCard(
                    titulo = titulo,
                    descripcion = descripcion,
                    completada = completadaMisionActualizada,
                    onCheckClicked = {
                        val userId = FirebaseAuth.getInstance().currentUser?.uid
                            ?: return@MisionDestacadaCard
                        val fechaHoy = LocalDate.now().toString()
                        val rutinaId = "${userId}_$fechaHoy"
                        val db = FirebaseFirestore.getInstance()

                        val rutinaRef = db.collection("rutinas").document(rutinaId)
                        val usuarioRef = db.collection("usuarios").document(userId)

                        db.runTransaction { transaction ->
                            val rutinaSnapshot = transaction.get(rutinaRef)
                            val usuarioSnapshot = transaction.get(usuarioRef)

                            val misionesActuales =
                                rutinaSnapshot.get("misiones") as? List<Map<String, Any>>
                                    ?: return@runTransaction

                            val misionActual =
                                misionesActuales.maxByOrNull { (it["puntos"] as? Long) ?: 0L }
                            val idMision =
                                misionActual?.get("id") as? String ?: return@runTransaction
                            val puntosMision = misionActual["puntos"] as? Long ?: 0L
                            val estabaCompletada = misionActual["completada"] as? Boolean ?: false

                            val nuevasMisiones = misionesActuales.map {
                                if (it["id"] == idMision) {
                                    it.toMutableMap().apply {
                                        this["completada"] = !estabaCompletada
                                    }
                                } else it
                            }

                            val expActual = usuarioSnapshot.getLong("exp") ?: 0L
                            val nivelActual = usuarioSnapshot.getLong("nivel") ?: 1L

                            val puntosGanados =
                                if (!estabaCompletada) puntosMision else -puntosMision
                            val (nuevaExp, nuevoNivel, nuevaExpMax) = calcularNivelYExperiencia(
                                expActual = expActual,
                                nivelActual = nivelActual,
                                puntos = puntosGanados
                            )

                            transaction.update(rutinaRef, "misiones", nuevasMisiones)
                            transaction.update(
                                usuarioRef,
                                mapOf(
                                    "exp" to nuevaExp,
                                    "nivel" to nuevoNivel,
                                    "expMax" to nuevaExpMax
                                )
                            )
                        }.addOnSuccessListener {
                            Log.d("MisionDestacada", "Misión actualizada correctamente")

                            coroutineScope.launch {
                                val nuevaRacha = recalcularRachaDesdeRutinas()

                                misionesState.value = obtenerRutinaDelDia()

                                val usuario = obtenerDatosUsuario()
                                expUsuario.value = usuario["exp"] as Long
                                nivelUsuario.value = usuario["nivel"] as Long
                                expMaxUsuario.value = usuario["expMax"] as Long
                                rachaUsuario.value = nuevaRacha

                                progresoSemana.value = obtenerProgresoSemanal()
                            }
                        }.addOnFailureListener {
                            Log.e("MisionDestacada", "Error: ${it.message}")
                        }
                    }
                )
            } else {
                Text("Cargando misión del día...", color = Color.White, modifier = Modifier.padding(10.dp))
            }


            CardMisiones(
                titulo = "Misiones",
                icono = Icons.Default.Checklist,
                onClick = {
                    navController.navigate(MenuInterna.MisionesDiarias.ruta) {
                        popUpTo(MenuInterna.Principal.ruta) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }

                }
            )

            CardCrearMision(
                icono = Icons.Default.AddCircle,
                onClick = {
                    navController.navigate(MenuInterna.MisionesCrear.ruta) {
                        popUpTo(MenuInterna.Principal.ruta) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }

                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(color = Color.Gray)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "📈 Progreso de la semana",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 15.sp,
                        color = Color.White
                    )
                )
            }

            WeeklyProgressGraph(dailyProgress = progresoSemana.value)

            Spacer(modifier = Modifier.height(8.dp))

            Text("\"${fraseDelDia.value}\"", color = Color.White, textAlign = TextAlign.Center, fontSize = 14.sp, fontStyle = FontStyle.Italic, modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp))
        }
    }
}

@Composable
fun BarraDeExperiencia(
    nivelActual: Int,
    experienciaActual: Int,
    experienciaNecesaria: Int
) {
    val progreso = experienciaActual.toFloat() / experienciaNecesaria

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Nivel: $nivelActual",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "$experienciaActual / $experienciaNecesaria EXP",
            color = Color.White,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LinearProgressIndicator(
            progress = progreso.coerceIn(0f, 1f),
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp),
            color = AzulBrillante,
            trackColor = Color.LightGray
        )
    }
}

@Composable
fun MisionDestacadaCard(
    titulo: String,
    descripcion: String,
    completada: Boolean,
    onCheckClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (completada) Color.DarkGray else Color(0xFF1E88E5)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = descripcion,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconToggleButton(
                checked = completada,
                onCheckedChange = { onCheckClicked() }
            ) {
                Icon(
                    imageVector = if (completada) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = if (completada) Color.Green else Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun CardMisiones(
    titulo: String,
    icono: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Morado)
    ) {
        Row(
            modifier = Modifier
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp,
                    color = Color.White
                )
            )
        }
    }
}

@Composable
fun CardCrearMision(
    icono: ImageVector,
    backgroundColor: Color = Color(0xFF7C24BE),
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 26.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "CREAR UNA NUEVA MISIÓN",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 15.sp,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeeklyProgressGraph(
    modifier: Modifier = Modifier,
    dailyProgress: List<Int>,
    maxMissions: Int = 4
) {
    val days = listOf("L", "M", "X", "J", "V", "S", "D")
    val barColor = Brush.verticalGradient(
        colors = listOf(Color.Cyan, Color.Blue)
    )
    val semanaActual = LocalDate.now().with(DayOfWeek.MONDAY)
    val formatoFecha = DateTimeFormatter.ofPattern("dd MMM")
    val fechaInicio = semanaActual.format(formatoFecha)
    val fechaFin = semanaActual.plusDays(6).format(formatoFecha)

    val rawIndex = LocalDate.now().dayOfWeek.value % 7
    val todayIndex = if (rawIndex == 0) 6 else rawIndex
    val diasContados = dailyProgress.take(todayIndex + 1)
    val suma = diasContados.sum()
    val maxTotal = maxMissions * diasContados.size
    val porcentaje = if (maxTotal > 0) {
        (suma.toFloat() / maxTotal.toFloat()) * 100
    } else {
        0f
    }
    val porcentajeFormateado = "${porcentaje.roundToInt()}%"

    Column(
        modifier = modifier
            .padding(16.dp)
            .background(color = Color.DarkGray, shape = RoundedCornerShape(16.dp))
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$fechaInicio - $fechaFin",
                color = Color.LightGray,
                fontSize = 12.sp,
            )

            Text(
                text = "$porcentajeFormateado",
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Bottom
        ) {
            dailyProgress.forEach { missions ->
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .width(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.DarkGray)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(fraction = missions / maxMissions.toFloat())
                            .clip(RoundedCornerShape(4.dp))
                            .background(barColor)
                            .align(Alignment.BottomCenter)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            days.forEach { day ->
                Text(
                    text = day,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.LightGray
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    val fakeNavController = rememberNavController()
    MenuScreenContent(fakeNavController)
}