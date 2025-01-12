package com.example.wallpaper_dead_reviewed.api.presentation.settingWallpaper

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.example.wallpaper_dead_reviewed.api.domain.entity.WallpaperLink
import kotlin.math.absoluteValue


@Composable
fun wallpaperPrevier(
    clickedIndex: Int,
    selectedWallpaperLink: String,
    nearbyWallpaperLinks: List<WallpaperLink>
) {
    val allWallpaperLinks = listOf(selectedWallpaperLink) + nearbyWallpaperLinks.map { it.wallpaperLink }
    val pagerState = rememberPagerState(initialPage = 0) { allWallpaperLinks.size }
    var showDialog by remember { mutableStateOf(false) }
    var currentBitmap by remember { mutableStateOf<Bitmap?>(null) }  // Holds the current bitmap

    LaunchedEffect(clickedIndex) {
        pagerState.scrollToPage(0)
    }

    val context = LocalContext.current

    // Update currentBitmap based on the current page
    LaunchedEffect(pagerState.currentPage) {
        val currentWallpaperLink = allWallpaperLinks[pagerState.currentPage]
        currentBitmap = loadImageAsBitmap(context, currentWallpaperLink)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.85f),
                contentPadding = PaddingValues(50.dp),
                key = { index -> "$index-${allWallpaperLinks[index]}" }
            ) { index ->
                val wallpaperLink = allWallpaperLinks[index]
                CardContent(
                    index = index,
                    pagerState = pagerState,
                    wallpaperLink = wallpaperLink,
                    onBitmapLoaded = { bitmap ->

                    }
                )
            }

            Spacer(modifier = Modifier.weight(0.05f))

            Button(
                onClick = { showDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Set Wallpaper")
            }
        }

        // Center the ArtDialog in the Box
        if (showDialog) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center // Center the dialog
            ) {
                ArtDialog(
                    showDialog = showDialog,
                    bitmap = currentBitmap,
                    onDismissRequest = { showDialog = false }
                )
            }
        }
    }
}

@Composable
fun CardContent(
    index: Int,
    pagerState: PagerState,
    wallpaperLink: String,
    onBitmapLoaded: (Bitmap?) -> Unit
) {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(wallpaperLink) {
        bitmap = loadImageAsBitmap(context, wallpaperLink)
        onBitmapLoaded(bitmap) // This can still be used for caching if needed
    }

    val pagerOffset = (pagerState.currentPage - index) + pagerState.currentPageOffsetFraction

    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(2.dp)
            .graphicsLayer {
                val scale = lerp(0.85f, 1f, 1f - pagerOffset.absoluteValue.coerceIn(0f, 1f))
                scaleX = scale
                scaleY = scale
                alpha = scale
            }
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = ImageRequest.Builder(context)
                .data(wallpaperLink)
                .crossfade(true)
                .scale(Scale.FILL)
                .build(),
            contentDescription = "Wallpaper image",
            contentScale = ContentScale.Crop
        )
    }
}
