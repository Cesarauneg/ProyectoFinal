package com.example.appproyecto.ui.screens

import android.graphics.drawable.Icon
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Output
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appproyecto.ui.navigation.Screen
import com.example.appproyecto.ui.theme.AzulBrillante
import com.example.appproyecto.ui.theme.AzulOscuro
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun PerfilScreen(navController: NavController) {
    val onConfigClick = { navController.navigate(Screen.Config.ruta) }

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val db = FirebaseFirestore.getInstance()

    var nombreUsuario by remember { mutableStateOf("Cargando...") }
    var categoriaActual by remember { mutableStateOf("Cargando...") }
    var nuevaCategoria by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        user?.uid?.let { uid ->
            val snapshot = db.collection("usuarios").document(uid).get().await()
            categoriaActual = snapshot.getString("categoria") ?: "No definida"
            nombreUsuario = snapshot.getString("nombre") ?: "Cargando..."
        }
    }

    Box(
        modifier = Modifier
            .background(
                color = AzulOscuro
            )
            .border(
                width = 4.dp,
                color = Color.White
            )
            .padding(bottom = 14.dp)
            .shadow(
                elevation = 10.dp,
                spotColor = AzulBrillante
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(54.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "👤 ${nombreUsuario}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                "Correo: ${user?.email}",
                fontSize = 16.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                "Categoría actual: $categoriaActual",
                fontSize = 16.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(color = Color.Gray)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = {
                onConfigClick()
            },colors = ButtonDefaults.buttonColors(AzulBrillante),
                modifier = Modifier.fillMaxWidth()) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Category,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Cambiar categoría",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 15.sp,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(onClick = {
                auth.signOut()
                navController.navigate("Login") {
                    popUpTo(0) { inclusive = true }
                }
            }, colors = ButtonDefaults.buttonColors(Color.Gray),
                modifier = Modifier.fillMaxWidth()) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Output,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Cerrar sesión",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 15.sp,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            var showDialog by remember { mutableStateOf(false) }

            if (showDialog) {
                androidx.compose.material3.AlertDialog(
                    onDismissRequest = { showDialog = false },
                    confirmButton = {
                        Button(
                            onClick = {
                                showDialog = false
                                user?.delete()?.addOnSuccessListener {
                                    Toast.makeText(context, "Cuenta eliminada", Toast.LENGTH_SHORT).show()
                                    navController.navigate("Login") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }?.addOnFailureListener {
                                    Toast.makeText(context, "Error al eliminar cuenta", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(Color.Red)
                        ) {
                            Text("Eliminar", color = Color.White)
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = { showDialog = false },
                            colors = ButtonDefaults.buttonColors(Color.Gray)
                        ) {
                            Text("Cancelar", color = Color.White)
                        }
                    },
                    title = {
                        Text("¿Eliminar cuenta?", fontWeight = FontWeight.Bold)
                    },
                    text = {
                        Text("Esta acción es irreversible. ¿Estás seguro de que deseas eliminar tu cuenta?")
                    },
                    containerColor = Color.DarkGray,
                    titleContentColor = Color.White,
                    textContentColor = Color.White
                )
            }

            Button(
                onClick = { showDialog = true },
                colors = ButtonDefaults.buttonColors(Color.Red),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Eliminar cuenta",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 15.sp,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                }
            }
        }
    }

}
