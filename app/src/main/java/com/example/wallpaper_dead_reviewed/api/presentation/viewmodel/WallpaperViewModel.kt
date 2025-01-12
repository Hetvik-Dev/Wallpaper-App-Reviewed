package com.example.wallpaper_dead_reviewed.api.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallpaper_dead_reviewed.api.Utils.Resource
import com.example.wallpaper_dead_reviewed.api.domain.entity.WallpaperLink
import com.example.wallpaper_dead_reviewed.api.domain.repository.WallpaperRepository
import com.example.wallpaper_dead_reviewed.api.presentation.WallPaperUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.wallpaper_dead_reviewed.R



@HiltViewModel
class WallpaperViewModel @Inject constructor(private val repository: WallpaperRepository) : ViewModel() {

    private val _wallpaperList = MutableStateFlow<WallPaperUiState>(WallPaperUiState.Loading)
    val wallpaperList: StateFlow<WallPaperUiState> = _wallpaperList.asStateFlow()

    private var currentPage = 5
    private val limit = 300
    private val allWallpapers = mutableListOf<WallpaperLink>()

    init {
        fetchWallpapers()
    }

    fun fetchWallpapers() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getImages(currentPage, limit).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        if (resource.data.isNullOrEmpty()) {
                            _wallpaperList.update { WallPaperUiState.EmptyList }
                        } else {
                            allWallpapers.addAll(resource.data)
                            _wallpaperList.update { WallPaperUiState.Success(allWallpapers) }
                            currentPage++ // Increment page after successful fetch
                        }
                    }
                    is Resource.Error -> {
                        _wallpaperList.update { WallPaperUiState.Error(resource.message ?: "Unknown Error") }
                    }
                }
            }
        }
    }
}
