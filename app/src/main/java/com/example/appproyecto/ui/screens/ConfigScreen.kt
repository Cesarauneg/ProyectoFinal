package com.example.appproyecto.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appproyecto.ui.navigation.Screen
import com.example.appproyecto.ui.theme.AzulBrillante
import com.example.appproyecto.ui.theme.AzulOscuro
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ConfigScreen(navController: NavController) {
    ConfigContent(
        onConfirmClick = { navController.navigate(Screen.Menu.ruta) }
    )
}

@Composable
fun ConfigContent(onConfirmClick: () -> Unit) {
    var seleccion by remember { mutableStateOf("") }
    var botonConfirmado by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .background(color = AzulOscuro)
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Text(
                    text = "¿Cuál es tu objetivo?",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Blue.copy(alpha = 0.7f),
                    style = TextStyle(
                        shadow = Shadow(
                            color = AzulBrillante,
                            offset = Offset(0f, 0f),
                            blurRadius = 18f
                        )
                    )
                )
                Text(
                    text = "¿Cuál es tu objetivo?",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    style = TextStyle(
                        shadow = Shadow(
                            color = AzulBrillante.copy(alpha = 0.7f),
                            offset = Offset(0f, 0f),
                            blurRadius = 18f
                        )
                    )
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Ayúdanos a entender mejor tus necesidades",
                fontSize = 15.sp,
                color = Color.White,
                style = TextStyle(
                    shadow = Shadow(
                        color = AzulBrillante.copy(alpha = 0.7f),
                        offset = Offset(0f, 0f),
                        blurRadius = 18f
                    )
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(modifier = Modifier
                .width(280.dp)
                .height(50.dp)
                .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(16.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (seleccion == "Salud y Fitness") AzulBrillante.copy(alpha = 0.5f) else Color.Black.copy(
                        alpha = 0.4f
                    )
                ),
                shape = RoundedCornerShape(16.dp),
                onClick = {
                    seleccion = "Salud y Fitness"
                }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("🔥", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Salud y Fitness", fontSize = 15.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(modifier = Modifier
                .width(280.dp)
                .height(50.dp)
                .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(16.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (seleccion == "Estudios y Aprendizaje") AzulBrillante.copy(
                        alpha = 0.5f
                    ) else Color.Black.copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(16.dp),
                onClick = {
                    seleccion = "Estudios y Aprendizaje"
                }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("🧠", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Estudios y Aprendizaje", fontSize = 15.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(modifier = Modifier
                .width(280.dp)
                .height(50.dp)
                .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(16.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (seleccion == "Trabajo y Productividad") AzulBrillante.copy(
                        alpha = 0.5f
                    ) else Color.Black.copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(16.dp),
                onClick = {
                    seleccion = "Trabajo y Productividad"
                }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("⌛", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Trabajo y Productividad", fontSize = 15.sp)
                }
            }
        }

        Button(
            onClick = {
                if (seleccion != "") {
                    botonConfirmado = true

                    val db = FirebaseFirestore.getInstance()
                    val userId = FirebaseAuth.getInstance().currentUser?.uid

                    if (userId != null){
                        db.collection("usuarios").document(userId)
                            .update("categoria", seleccion)
                            .addOnSuccessListener {
                                Log.d("Firestore", "Categoria actualizada correctamente")
                            }
                            .addOnFailureListener { e ->
                                Log.w("Firestore", "Error al actualizar la categoria", e)
                            }
                    }

                    onConfirmClick()
                } else {
                    Toast.makeText(
                        context,
                        "Tienes que elegir una opción",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 60.dp, end = 24.dp)
                .width(120.dp)
                .height(50.dp)
                .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(16.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (botonConfirmado) AzulBrillante.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.4f)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box {
                Text(
                    "Confirmar",
                    fontSize = 15.sp,
                    color = Color.Blue.copy(alpha = 0.7f),
                    style = TextStyle(
                        shadow = Shadow(
                            color = AzulBrillante,
                            offset = Offset(0f, 0f),
                            blurRadius = 18f
                        )
                    ),
                )
                Text(
                    "Confirmar",
                    fontSize = 15.sp,
                    color = Color.White,
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
                    "Confirmar",
                    fontSize = 15.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        shadow = Shadow(
                            color = AzulBrillante,
                            offset = Offset(0f, 0f),
                            blurRadius = 18f
                        )
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun ConfigScreenPreview() {
    ConfigContent(onConfirmClick = {})
}