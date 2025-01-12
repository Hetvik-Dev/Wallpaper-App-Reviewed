package com.example.wallpaper_dead_reviewed.api.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wallpaper_dead_reviewed.api.domain.entity.WallpaperLink
import com.example.wallpaper_dead_reviewed.R
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ImagesRecyclerViewAdapter(
    private var dataSet: List<WallpaperLink>,
    private val onWallpaperItemClick: (Int, String, List<String>) -> Unit // Update the lambda to accept the index
) : RecyclerView.Adapter<ImagesRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: AppCompatImageView = view.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.text_row_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        Glide.with(viewHolder.imageView.context)
            .load(dataSet[position].wallpaperLink)
            .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache all images
            .into(viewHolder.imageView)

        viewHolder.imageView.setOnClickListener {

            val startIndex = maxOf(0, position - 9)
            val endIndex = minOf(dataSet.size - 1, position + 9)


            val nearbyLinks = dataSet.subList(startIndex, endIndex + 1).map { it.wallpaperLink }

            onWallpaperItemClick(position, dataSet[position].wallpaperLink, nearbyLinks)
        }
    }


    override fun getItemCount() = dataSet.size
}