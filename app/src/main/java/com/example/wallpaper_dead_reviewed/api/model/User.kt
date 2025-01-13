package com.example.wallpaper_dead_reviewed.api.model

data class User(
    val name: String,
    val email: String,
    val profilePictureUrl: String,
    val username: String,
    val uploadedImages: List<String>
)