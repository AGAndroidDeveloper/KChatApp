package com.ankitgupta.kchatapp.utill

import android.content.Context
import android.widget.ImageView
import com.ankitgupta.kchatapp.R
import com.bumptech.glide.Glide

class GlideLoadImage(private val context: Context) {
    fun loadImage(imageView: ImageView, imageUrl: String) {
        Glide.with(context)
            .load(imageUrl)
            .centerCrop()
            .placeholder(R.drawable.pngtree_gray_avatar_placeholder_png_image_3416697)
            .error(R.drawable.pngtree_gray_avatar_placeholder_png_image_3416697)
            .into(imageView)
    }

    fun loadLogOutGif(logoutImage: ImageView) {
        Glide.with(context)
            .load(R.drawable.icons8_logout)
            .centerCrop()
            .into(logoutImage)
    }

}