package com.example.wallpaper_dead_reviewed.api.data

import com.example.wallpaper_dead_reviewed.api.Utils.Constants.END_POINT
import com.example.wallpaper_dead_reviewed.api.model.PicSumItem
import retrofit2.http.GET
import retrofit2.http.Query


interface PicSumApi {

    @GET(END_POINT)
    suspend fun getWallpaperImages(
        @Query("page") page: Int = 5,
        @Query("limit") limit: Int = 300,
        @Query("width") width: Int = 1080, // common width for modern Android phones
        @Query("height") height: Int = 2244 // common height for modern Android phones
    ): List<PicSumItem>?

}