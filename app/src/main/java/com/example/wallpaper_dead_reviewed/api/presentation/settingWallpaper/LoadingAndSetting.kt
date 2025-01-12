package com.example.wallpaper_dead_reviewed.api.presentation.settingWallpaper

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
//import android.content.Context
//import android.graphics.Bitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult


suspend fun setWallpaperAsync(context: Context, bitmap: Bitmap, flag: Int) {
    val wallpaperManager = WallpaperManager.getInstance(context)
    withContext(Dispatchers.IO) {
        try {
            if (flag == WallpaperManager.FLAG_SYSTEM || flag == WallpaperManager.FLAG_LOCK || flag == (WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK)) {
                wallpaperManager.setBitmap(bitmap, null, true, flag)
            } else {
                wallpaperManager.setBitmap(bitmap) // Default to system wallpaperPrevier if no valid flag
            }
        } catch (e: Exception) {
            e.printStackTrace() // Log or handle the error
        }
    }
}


suspend fun loadImageAsBitmap(context: Context, imageUrl: String): Bitmap? {
    val imageLoader = ImageLoader(context)
    val request = ImageRequest.Builder(context)
        .data(imageUrl)
        .build()

    return try {
        val result = imageLoader.execute(request)
        if (result is SuccessResult) {
            (result.drawable as? android.graphics.drawable.BitmapDrawable)?.bitmap
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

