package com.example.wallpaper_dead_reviewed.api.presentation.composables.LottieComposables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.wallpaper_dead_reviewed.R

@Composable
fun LottieBackground(content: @Composable () -> Unit) {
    val compositionResult = rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.new_background)
    )

    val composition = compositionResult.value
    val isLoading = compositionResult.isLoading

    Box(modifier = Modifier.fillMaxSize()) {
        if (composition != null && !isLoading) {
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever, // Loop the animation
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                // .padding(1.dp) // Uncomment if you want to crop the animation by 1 dp
            )
        }

        content()
    }
}