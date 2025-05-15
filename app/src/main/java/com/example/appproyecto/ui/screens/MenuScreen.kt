package com.example.appproyecto.ui.screens

import android.os.Build
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import com.example.appproyecto.R
import com.example.appproyecto.ui.navigation.Screen
import com.example.appproyecto.ui.theme.AzulBrillante
import com.example.appproyecto.ui.theme.AzulOscuro
import com.example.appproyecto.ui.theme.Morado
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt
import java.time.DayOfWeek

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
                startDestination = "menu_principal"
            ) {
                composable("menu_principal") {
                    MenuScreenContent(
                        onConfirmClick = { navController.navigate(Screen.Menu.ruta) }
                    )
                }
                composable("misiones") {
                    MisionScreen()
                }
            }
        }
    }
}

object RutinaActual {
    var misiones: List<Map<String, Any>> = emptyList()
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Inicio", "menu_principal", Icons.Default.Home),
        BottomNavItem("Misiones", "misiones", Icons.Default.Checklist)
    )

    NavigationBar(
        containerColor = Color.DarkGray,
        tonalElevation = 8.dp,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            val selected = currentRoute == item.route
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
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
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
@Composable
fun MenuScreenContent(onConfirmClick: () -> Unit) {
    val semana = listOf(4, 6, 5, 5, 5, 0, 0)


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
                    modifier = Modifier
                        .size(40.dp)
                )

                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "Usuario",
                    fontSize = 15.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.width(20.dp))

                Text("🔥", fontSize = 15.sp)
                Spacer(modifier = Modifier.width(5.dp))
                Text("1", fontSize = 15.sp, color = Color.White)
            }



            BarraDeExperiencia(5, 10, 100)

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

            val misionesDelDia = emptyList<Map<String, Any>>()

            var misionMayorPuntaje = misionDestacada(misionesDelDia)

            val id = misionMayorPuntaje["id"] as? String ?: "Sin id"
            val titulo = misionMayorPuntaje["titulo"] as? String ?: "Sin titulo"
            val descripcion = misionMayorPuntaje["descripcion"] as? String ?: "Sin descripcion"
            val completadaMision = misionMayorPuntaje["completada"] as? Boolean ?: false

            MisionDestacadaCard(
                titulo = titulo,
                descripcion = descripcion,
                completada = completadaMision,
                onCheckClicked = {}
            )



            CardMisiones(
                titulo = "Misiones",
                icono = Icons.Default.Checklist,
                onClick = {}
            )

            CardCrearMision(
                icono = Icons.Default.AddCircle,
                onClick = {}
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

            WeeklyProgressGraph(dailyProgress = semana)

            Spacer(modifier = Modifier.height(8.dp))

            Text("\"Frase motivacional del día\"", color = Color.White, fontSize = 15.sp)
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
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Morado)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp),
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
    maxMissions: Int = 6
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
    val diasContados = dailyProgress.take(todayIndex)
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
@Preview
@Composable
fun MenuScreenPreview() {
    MenuScreenContent(onConfirmClick = {})
}