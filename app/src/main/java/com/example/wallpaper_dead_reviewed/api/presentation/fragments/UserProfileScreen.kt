package com.example.wallpaperapp.presentation.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
//import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
//import androidx.fragment.app.Fragment

import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import coil.compose.rememberImagePainter
//import com.example.wallpaperapp.fireBase.User

//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.airbnb.lottie.LottieComposition
//import androidx.compose.ui.tooling.preview.Preview

import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.wallpaper_dead_reviewed.R.raw
import com.example.wallpaper_dead_reviewed.api.presentation.view.LoginActivity
import com.example.wallpaper_dead_reviewed.api.model.User
import com.google.firebase.storage.FirebaseStorage

class UserProfileScreen : Fragment() {

    private lateinit var userName: String
    private lateinit var userEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userName = arguments?.getString("USER_NAME") ?: "Unknown"
        userEmail = arguments?.getString("USER_EMAIL") ?: "No Email"
        Log.d("User ProfileScreen", "User  Name: $userName, User Email: $userEmail")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("User ProfileScreen", "User  Name: $userName, User Email: $userEmail")

        val user = User(
            name = userName,
            email = userEmail,
            profilePictureUrl = "https://picsum.photos/id/237/200/300",
            username = userName,
            uploadedImages = List(5) { "https://picsum.photos/seed/picsum/200/300" }
        )

        // Ensure Compose integration is correct
        return ComposeView(requireContext()).apply {
            setContent {
                UserProfileScreenContent(user = user)
            }
        }
    }

    @Composable
    fun UserProfileScreenContent(user: User?) {
        val context = LocalContext.current

//    val backgroundComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.artshpere_background))
//    val backgroundProgress by animateLottieCompositionAsState(backgroundComposition, iterations = LottieConstants.IterateForever)

        var isPlaying by remember { mutableStateOf(true) }
        var profilePictureUrl by remember { mutableStateOf(user?.profilePictureUrl) }
        var showDialog by remember { mutableStateOf(false) }
        var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

        val getImageLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    selectedImageUri = it
                    uploadImageToFirebase(it) { downloadUrl ->
                        profilePictureUrl = downloadUrl
                    }
                    showDialog = false // Close the dialog
                }
            }

        val composition: LottieComposition? by rememberLottieComposition(
            LottieCompositionSpec.RawRes(resId = raw.hello_animate)
        )

        // Control the animation progress
        val progress by animateLottieCompositionAsState(
            composition,
            iterations = LottieConstants.IterateForever, // This makes the animation loop forever
            isPlaying = isPlaying
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Lottie Animation as background
//        LottieAnimation(
//            composition = backgroundComposition,
//            progress = backgroundProgress,
//            modifier = Modifier.fillMaxSize()
//        )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 50.dp), // Padding from the status bar
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                user?.profilePictureUrl?.let { imageUrl ->
                    Image(
                        painter = rememberImagePainter(imageUrl),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape)
                            .clickable {
                                showDialog = true
                            }

                    )
                }

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text(text = "Change Profile Picture") },
                        text = {
                            Column {
                                Text("Choose an option:")
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(onClick = {

                                    getImageLauncher.launch("image/*")

                                }) {
                                    Text("Select from Gallery")
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(onClick = {
                                    showDialog = false
                                }) {
                                    Text("Take a New Photo")
                                }
                            }
                        },
                        confirmButton = {
                            Button(onClick = { showDialog = false }) {
                                Text("Cancel")
                            }
                        }
                    )
                }

                Text(
                    text = userName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = userEmail,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    items(user?.uploadedImages ?: emptyList()) { imageUrl ->
                        Image(
                            painter = rememberImagePainter(imageUrl),
                            contentDescription = "Uploaded Image",
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .padding(4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val intent = Intent(context, LoginActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                ) {
                    Text(
                        text = "Login",
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                LottieAnimation(
                    composition = composition,
                    progress = progress,
                    modifier = Modifier
                        .size(200.dp)
                        .padding(top = 16.dp) // Add some padding above the animation
                )
            }
        }
    }

    private fun uploadImageToFirebase(uri: Uri, onUploadComplete: (String) -> Unit) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val fileRef =
            storageRef.child("profile_pictures/${System.currentTimeMillis()}.jpg") // Create a unique file name

        fileRef.putFile(uri)
            .addOnSuccessListener {
                // Get the download URL after the upload is successful
                fileRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    onUploadComplete(downloadUrl.toString()) // Return the download URL
                }.addOnFailureListener { exception ->
                    // Handle any errors while getting the download URL
                    exception.printStackTrace()
                }
            }.addOnFailureListener { exception ->
                // Handle any errors during the upload
                exception.printStackTrace()
            }
    }
}