package com.example.wallpaper_dead_reviewed.api.domain.repository

import com.example.wallpaper_dead_reviewed.api.Utils.Resource
import com.example.wallpaper_dead_reviewed.api.domain.entity.WallpaperLink
import kotlinx.coroutines.flow.Flow

interface WallpaperRepository {

  fun getImages(page: Int, limit: Int) : Flow<Resource<List<WallpaperLink>>>
}
