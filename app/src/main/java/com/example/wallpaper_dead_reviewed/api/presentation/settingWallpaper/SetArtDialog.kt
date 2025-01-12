package com.example.wallpaper_dead_reviewed.api.presentation.settingWallpaper

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ArtDialog(
    showDialog: Boolean,
    bitmap: Bitmap?,
    onDismissRequest: () -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    if (showDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Choose where to set the wallpaper",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    var scaleHome by remember { mutableStateOf(1f) }
                    var scaleLock by remember { mutableStateOf(1f) }
                    var scaleCancel by remember { mutableStateOf(1f) }
                    val buttonWidth = 200.dp

                    // Set Home Screen Button
                    Button(
                        onClick = {
                            scaleHome = 0.9f
                            coroutineScope.launch {
                                bitmap?.let {
                                    setWallpaperAsync(context, it, WallpaperManager.FLAG_SYSTEM)
                                    Toast.makeText(
                                        context,
                                        "Home Screen Wallpaper Set!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                onDismissRequest()
                            }
                        },
                        modifier = Modifier
                            .width(buttonWidth)
                            .padding(vertical = 8.dp)
                            .graphicsLayer(scaleX = scaleHome, scaleY = scaleHome)
                            .animateContentSize(),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EE))
                    ) {
                        Text("Set Home Screen", color = Color.White)
                    }

                    // Set Lock Screen Button
                    Button(
                        onClick = {
                            scaleLock = 0.9f
                            coroutineScope.launch {
                                bitmap?.let {
                                    setWallpaperAsync(context, it, WallpaperManager.FLAG_LOCK)
                                    Toast.makeText(
                                        context,
                                        "Lock Screen Wallpaper Set!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                onDismissRequest()
                            }
                        },
                        modifier = Modifier
                            .width(buttonWidth)
                            .padding(vertical = 8.dp)
                            .graphicsLayer(scaleX = scaleLock, scaleY = scaleLock)
                            .animateContentSize(),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EE))
                    ) {
                        Text("Set Lock Screen", color = Color.White)
                    }

                    // Cancel Button
                    TextButton(
                        onClick = {
                            scaleCancel = 0.9f
                            coroutineScope.launch {
                                delay(100)
                                onDismissRequest()
                            }
                        },
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .graphicsLayer(scaleX = scaleCancel, scaleY = scaleCancel)
                            .animateContentSize()
                    ) {
                        Text("Cancel", color = Color.Gray)
                    }
                }
            }
        }
    }
}