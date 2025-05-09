package com.example.appproyecto.ui.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.appproyecto.ui.navigation.Screen
import com.example.appproyecto.ui.theme.AzulBrillante
import com.example.appproyecto.ui.theme.AzulOscuro
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView

@Composable
fun WelcomeScreen(navController: NavController) {
    VideoBackground(
        onLoginClick = { navController.navigate(Screen.Login.ruta) },
        onRegisterClick = { navController.navigate(Screen.Register.ruta) }
    )
}


@Composable
fun VideoBackground(onLoginClick: () -> Unit, onRegisterClick: () -> Unit) {
    val context = LocalContext.current
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            val uri = Uri.parse("android.resource://${context.packageName}/raw/fondo3")
            val mediaItem = MediaItem.fromUri(uri)
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
            repeatMode = Player.REPEAT_MODE_ALL
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Fondo de video
        AndroidView(
            factory = {
                PlayerView(context).apply {
                    this.player = player
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Capa semitransparente y contenido encima del video
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AzulOscuro.copy(alpha = 0.5f)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
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

            Button(
                modifier = Modifier
                    .width(280.dp)
                    .height(50.dp)
                    .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(16.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black.copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(16.dp),
                onClick = onLoginClick
            ) {
                Box {
                    Text(
                        "Iniciar sesión",
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
                        "Iniciar sesión",
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
                        "Iniciar sesión",
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

            Button(
                modifier = Modifier
                    .width(280.dp)
                    .height(50.dp)
                    .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(16.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulBrillante
                ),
                shape = RoundedCornerShape(16.dp),
                onClick = onRegisterClick
            ) {
                Box {
                    Text(
                        "Registrarse",
                        fontSize = 15.sp,
                        color = Color.Blue.copy(alpha = 0.7f),
                        style = TextStyle(
                            shadow = Shadow(
                                color = AzulOscuro,
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
                                color = AzulOscuro,
                                offset = Offset(0f, 0f),
                                blurRadius = 18f
                            )
                        )
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun WelcomePreview() {
    VideoBackground(onLoginClick = {}, onRegisterClick = {})
}