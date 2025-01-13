package com.example.wallpaper_dead_reviewed.api.presentation.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.wallpaper_dead_reviewed.R
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID


class ArtFragment : Fragment() {

    @Preview(showBackground = true)
    @Composable
    fun PreviewMeScreen() {
        ArtScreen()
    }

    private var selectedImageUri: Uri? = null
    private var isUploading by mutableStateOf(false)

    private val getImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                Toast.makeText(context, "Image Selected!", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
//                LottieBackground {
                ArtScreen()
//                }
            }
        }
    }

    @Composable
    fun ArtScreen() {
        val context = LocalContext.current

//        // Lottie Animation for background
//        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.artshpere_background))
//        val progress by animateLottieCompositionAsState(
//            composition,
//            iterations = LottieConstants.IterateForever
//        )

        Box(
            modifier = Modifier
                .fillMaxSize() // Ensure it fills the entire screen
                .background(Color.White), // Make background transparent
            contentAlignment = Alignment.Center
        ) {
//            LottieAnimation(
//                composition = composition,
//                iterations = LottieConstants.IterateForever,
//                modifier = Modifier.fillMaxSize()
//            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(), // This should fill the width of the Box
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Upload Art",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                Button(
                    onClick = { openImageChooser() },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Text(text = "Select Image")
                }

                Button(
                    onClick = { uploadImageToFirebase(context) },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Upload Image")
                }

                if (isUploading) {
                    // Show progress indicator while uploading
                    CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
                }
            }
        }
    }

    private fun openImageChooser() {
        getImageLauncher.launch("image/*")
    }

    private fun uploadImageToFirebase(context: Context) {
        if (selectedImageUri != null) {
            isUploading = true
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("uploads/${UUID.randomUUID()}.jpg")

            imageRef.putFile(selectedImageUri!!)
                .addOnSuccessListener {
                    isUploading = false
                    Toast.makeText(context, "Upload Successful!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    isUploading = false
                    Toast.makeText(context, "Upload Failed: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
        } else {
            Toast.makeText(context, "Please select an image first", Toast.LENGTH_SHORT).show()
        }
    }
}