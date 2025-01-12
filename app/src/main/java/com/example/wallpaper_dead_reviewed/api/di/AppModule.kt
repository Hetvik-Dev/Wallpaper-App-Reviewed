package com.example.wallpaper_dead_reviewed.api.di

import com.example.wallpaper_dead_reviewed.api.Utils.Constants.BASE_URL
import com.example.wallpaper_dead_reviewed.api.data.PicSumApi
import com.example.wallpaper_dead_reviewed.api.data.WallpaperRepostiryImpl
import com.example.wallpaper_dead_reviewed.api.domain.repository.WallpaperRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// Step 1 CLEAN PROJECT
@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    companion object {
        @Provides
        @Singleton
        fun provideRetrofitApi(): PicSumApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // A
                .build().create(PicSumApi::class.java)
        }

        @Provides
        @Singleton
        fun provideWallpaperRepositoryImpl(picSumApi: PicSumApi): WallpaperRepostiryImpl {
            return WallpaperRepostiryImpl(picSumApi)
        }
    }

    @Binds
    @Singleton
    fun bindWallpaperRepository(repositoryImpl: WallpaperRepostiryImpl): WallpaperRepository
}