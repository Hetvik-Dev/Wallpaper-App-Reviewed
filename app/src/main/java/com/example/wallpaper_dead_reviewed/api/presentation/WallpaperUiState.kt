package com.example.wallpaper_dead_reviewed.api.presentation

import com.example.wallpaper_dead_reviewed.api.domain.entity.WallpaperLink

sealed class WallPaperUiState{
    object Loading : WallPaperUiState()
    object EmptyList : WallPaperUiState()

    // update data when there is success
    data class Success(val data: List<WallpaperLink>) : WallPaperUiState()

    // don't update data when there is error
    data class Error(val message:String) : WallPaperUiState()
}
