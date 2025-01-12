package com.example.wallpaper_dead_reviewed.api.data

import android.util.Log
import com.example.wallpaper_dead_reviewed.api.Utils.Resource
import com.example.wallpaper_dead_reviewed.api.domain.entity.WallpaperLink
import com.example.wallpaper_dead_reviewed.api.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class WallpaperRepostiryImpl @Inject constructor(val picSumApi: PicSumApi) : WallpaperRepository {

    override fun getImages(page: Int, limit: Int): Flow<Resource<List<WallpaperLink>>> = flow {
        try {
            val response = picSumApi.getWallpaperImages(page, limit)

            response?.let {
                val wallpaperLinks: List<WallpaperLink> = response.map {
                    WallpaperLink(it.downloadUrl.orEmpty())
                }
                emit(Resource.Success(wallpaperLinks))
            }
        } catch (e: Exception) {
            Log.e("Error", "Error fetching wallpaperPrevier images: $e")
            var errorOutput = e.message ?: "Unknown Error"
            emit(Resource.Error(null, errorOutput))
        }
    }
}
