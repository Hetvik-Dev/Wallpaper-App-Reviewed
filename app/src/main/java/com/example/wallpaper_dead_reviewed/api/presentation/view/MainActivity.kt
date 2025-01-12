package com.example.wallpaper_dead_reviewed.api.presentation.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.example.wallpaper_dead_reviewed.R
import com.example.wallpaper_dead_reviewed.databinding.ActivityMainBinding
import com.example.wallpaper_dead_reviewed.api.presentation.adapter.ImagesRecyclerViewAdapter
import com.example.wallpaper_dead_reviewed.api.domain.entity.WallpaperLink
import com.example.wallpaper_dead_reviewed.api.presentation.WallPaperUiState
import com.example.wallpaper_dead_reviewed.api.presentation.viewmodel.WallpaperViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.wallpaper_dead_reviewed.api.presentation.settingWallpaper.imageViewToComposable
import com.example.wallpaper_dead_reviewed.api.presentation.fragments.ArtFragment
import com.example.wallpaperapp.presentation.fragments.UserProfileScreen
//import com.example.wallpaper_dead_reviewed.api.presentation.fragments.
import com.facebook.shimmer.Shimmer
import com.google.firebase.FirebaseApp

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val wallpaperViewModel: WallpaperViewModel by viewModels()
    private lateinit var lottieView: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        FirebaseApp.initializeApp(this)
        setContentView(binding.root)

        lottieView = findViewById<LottieAnimationView>(R.id.lottieBackground)

        getSupportActionBar()?.setDisplayShowTitleEnabled(false)

        lottieView.playAnimation()

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigate_arts -> {
                    replaceFragment(ArtFragment())
                    true
                }
                R.id.navigate_me -> {
                    replaceFragment(UserProfileScreen())
                    true
                }
                R.id.navigate_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        setupViews()
        collectUiState()
        wallpaperViewModel.fetchWallpapers()
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    private fun setupViews() {
        binding.imagesRecyclerView.apply {
            // Using GridLayoutManager for 2 columns
            layoutManager = GridLayoutManager(context, 2)  // 2 columns
            adapter = ImagesRecyclerViewAdapter(emptyList()) { clickedIndex, wallpaperLink, nearbyLinks ->
                onClickImage(clickedIndex, wallpaperLink, nearbyLinks)
            }
        }
    }

    private fun collectUiState() {
        val shimmerLayout = binding.shimmerViewContainer.setShimmer(
            Shimmer.AlphaHighlightBuilder()
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setDuration(1000)
                .build()
        )
        lifecycleScope.launch(Dispatchers.Main) {
            wallpaperViewModel.wallpaperList.collect { wallpaperUiState ->
                when (wallpaperUiState) {
                    is WallPaperUiState.Loading -> {
                        shimmerLayout.startShimmer()
                        shimmerLayout.visibility = View.VISIBLE
                        binding.imagesRecyclerView.visibility = View.GONE
                    }   

                    is WallPaperUiState.Success -> {
                        shimmerLayout.stopShimmer()
                        shimmerLayout.visibility = View.GONE
                        binding.imagesRecyclerView.visibility = View.VISIBLE
                        populateDataInRecyclerView(wallpaperUiState.data)
                    }

                    is WallPaperUiState.EmptyList, is WallPaperUiState.Error -> {
                        shimmerLayout.stopShimmer()
                        shimmerLayout.visibility = View.GONE
                        binding.imagesRecyclerView.visibility = View.GONE
                    }
                }
            }
        }
    }


    private fun populateDataInRecyclerView(list: List<WallpaperLink>) {
        val wallpaperAdapter = ImagesRecyclerViewAdapter(list) { clickedIndex, wallpaperLink, nearbyLinks ->
            onClickImage(clickedIndex, wallpaperLink, nearbyLinks)
        }
        binding.imagesRecyclerView.adapter = wallpaperAdapter
    }

    private fun onClickImage(clickedIndex: Int, wallpaperLink: String, nearbyLinks: List<String>) {
        val intent = Intent(this, imageViewToComposable::class.java).apply {
            putExtra("CLICKED_INDEX", clickedIndex)
            putExtra("WALLPAPER_LINK", wallpaperLink)
            putExtra("NEARBY_WALLPAPER_LINKS", nearbyLinks.toTypedArray())
        }
        this.startActivity(intent)
    }
}
