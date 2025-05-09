package com.example.appproyecto.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavController
import com.example.appproyecto.ui.navigation.Screen
import com.example.appproyecto.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun RegisterScreen(navController: NavController) {
    RegisterContent (
        onLoginClick = { navController.navigate(Screen.Login.ruta) {popUpTo(Screen.Welcome.ruta)} },
        onConfigClick = { navController.navigate(Screen.Config.ruta) {popUpTo(Screen.Register.ruta) { inclusive = true} } }
    )
}

@Composable
fun RegisterContent(onLoginClick: () -> Unit, onConfigClick: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

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
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                // Glow "falso", atrás
                Text(
                    text = "Level Up App",
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
                // Texto principal, encima
                Text(
                    text = "Level Up App",
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

            Spacer(modifier = Modifier.height(32.dp))

            Box {
                Text(
                    text = "Crea tu cuenta",
                    fontSize = 20.sp,
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
                    text = "Crea tu cuenta",
                    fontSize = 20.sp,
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


            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre", color = Color.White.copy(alpha = 0.8f)) },
                singleLine = true,
                placeholder = {
                    Text(
                        "Tu nombre de usuario",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 15.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = if (nombre.isBlank()) Color.Gray else Color.White
                    )
                },
                modifier = Modifier
                    .height(60.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.White,
                    focusedBorderColor = AzulBrillante,
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                shape = MaterialTheme.shapes.large,
                textStyle = LocalTextStyle.current.copy(fontSize = 15.sp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo electrónico", color = Color.White.copy(alpha = 0.8f)) },
                singleLine = true,
                placeholder = {
                    Text(
                        "correo@ejemplo.com",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 15.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = if (correo.isBlank()) Color.Gray else Color.White
                    )
                },
                modifier = Modifier
                    .height(60.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.White,
                    focusedBorderColor = AzulBrillante,
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                shape = MaterialTheme.shapes.large,
                textStyle = LocalTextStyle.current.copy(fontSize = 15.sp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña", color = Color.White.copy(alpha = 0.8f)) },
                singleLine = true,
                placeholder = {
                    Text(
                        "Tu contraseña",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 15.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (password.isBlank()) Color.Gray else Color.White
                    )
                },
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Default.Visibility
                    else Icons.Default.VisibilityOff

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = image,
                            contentDescription = null,
                            tint = if (passwordVisible) Color.White else Color.Gray
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .height(60.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.White,
                    focusedBorderColor = AzulBrillante,
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                shape = MaterialTheme.shapes.large,
                textStyle = LocalTextStyle.current.copy(fontSize = 15.sp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar contraseña", color = Color.White.copy(alpha = 0.8f)) },
                singleLine = true,
                placeholder = {
                    Text(
                        "Repite tu contraseña",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 15.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = if (confirmPassword.isBlank()) Color.Gray else Color.White
                    )
                }, trailingIcon = {
                    val image = if (confirmPasswordVisible)
                        Icons.Default.Visibility
                    else Icons.Default.VisibilityOff

                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = image,
                            contentDescription = null,
                            tint = if (confirmPasswordVisible) Color.White else Color.Gray
                        )
                    }
                },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .height(60.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.White,
                    focusedBorderColor = AzulBrillante,
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                shape = MaterialTheme.shapes.large,
                textStyle = LocalTextStyle.current.copy(fontSize = 15.sp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(modifier = Modifier
                .width(280.dp)
                .height(50.dp)
                .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(16.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black.copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(16.dp),
                onClick = {
                    // Aquí podrías validar o navegar
                    when {
                        nombre.isBlank() || correo.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
                            // Mostrar error: "Todos los campos son obligatorios"
                            Toast.makeText(
                                context,
                                "Todos los campos son obligatorios",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        password != confirmPassword -> {
                            // Mostrar error: "Las contraseñas no coinciden"
                            Toast.makeText(
                                context,
                                "Las contraseñas no coinciden",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {
                            // Llamar a Firebase para registrar usuario
                            val auth = FirebaseAuth.getInstance()
                            auth.createUserWithEmailAndPassword(correo, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(
                                            context,
                                            "Usuario creado correctamente",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        // Aquí puedes navegar a otra pantalla si quieres
                                        val db = FirebaseFirestore.getInstance()
                                        val userId = FirebaseAuth.getInstance().currentUser?.uid

                                        val usuario = hashMapOf(
                                            "nombre" to nombre,
                                            "correo" to correo,
                                            "categoria" to "",
                                            "nivel" to 1,
                                            "exp" to 0,
                                            "expMax" to 100
                                        )

                                        if (userId != null) {
                                            db.collection("usuarios")
                                                .document(userId)
                                                .set(usuario)
                                                .addOnSuccessListener {
                                                    Log.d("Firestore", "Usuario guardado exitosamente")
                                                }
                                                .addOnFailureListener { e ->
                                                    Log.w("Firestore", "Error al guardar usuario", e)
                                                }
                                        }

                                        onConfigClick()
                                    } else {
                                        val error = task.exception?.message ?: "Error al registrar"
                                        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                                    }
                                }
                        }
                    }
                }) {
                Box {
                    Text(
                        "Registrarse",
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
                        "Registrarse",
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
                        "Registrarse",
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

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "¿Ya tienes cuenta?",
                color = Color.White,
                fontSize = 14.sp
            )
            Box(
                modifier = Modifier
                    .clickable {
                        onLoginClick()
                    }
            ) {
                Text(
                    text = "Iniciar sesión",
                    fontSize = 15.sp,
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
                    text = "Iniciar sesión",
                    fontSize = 15.sp,
                    color = Color.White,
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
fun RegisterScreenPreview() {
    RegisterContent(onLoginClick = {}, onConfigClick = {})
}