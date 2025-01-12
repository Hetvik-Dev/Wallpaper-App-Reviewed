package com.example.wallpaper_dead_reviewed.api.presentation.settingWallpaper

// ComposeCardActivity.kt
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
//import com.example.wallpaper_dead_reviewed.api.Utils.
import com.example.wallpaper_dead_reviewed.api.domain.entity.WallpaperLink


class imageViewToComposable : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val clickedIndex = intent.getIntExtra("CLICKED_INDEX", 0)
            val wallpaperLink = intent.getStringExtra("WALLPAPER_LINK") ?: ""
            val nearbyWallpaperLinks =
                intent.getStringArrayExtra("NEARBY_WALLPAPER_LINKS")?.toList() ?: emptyList()

            // Assuming WallpaperLink is a simple data class
            val nearbyWallpaperLinkObjects = nearbyWallpaperLinks.map { WallpaperLink(it) }

            Log.d("ComposeCardActivity", "Clicked index: $clickedIndex")
            Log.d("ComposeCardActivity", "Nearby images: $nearbyWallpaperLinks")

            wallpaperPrevier(clickedIndex, wallpaperLink, nearbyWallpaperLinkObjects)
        }
    }
}


