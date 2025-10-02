package com.example.videoplayerhub.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.videoplayerhub.R
import com.example.videoplayerhub.model.FavoritePhoto

class FavoriteAdapter(
    private val items: List<FavoritePhoto>
) : RecyclerView.Adapter<FavoriteAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgThumb)
        val tvAuthor: TextView = view.findViewById(R.id.tvAuthor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return VH(v)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val fav = items[position]
        Glide.with(holder.itemView).load(fav.downloadUrl).into(holder.img)
        holder.tvAuthor.text = fav.author
    }
}
