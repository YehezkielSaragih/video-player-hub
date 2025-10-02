package com.example.videoplayerhub.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.videoplayerhub.R
import com.example.videoplayerhub.model.FavoritePhoto

class FavoriteAdapter(
    private val items: MutableList<FavoritePhoto>,
    private val onRemove: (FavoritePhoto) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgFavThumb)
        val tvAuthor: TextView = view.findViewById(R.id.tvFavAuthor)
        val btnRemove: ImageButton = view.findViewById(R.id.btnRemoveFav)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false)
        return VH(v)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val fav = items[position]
        Glide.with(holder.itemView)
            .load(fav.downloadUrl)
            .placeholder(android.R.color.darker_gray)
            .into(holder.img)

        holder.tvAuthor.text = fav.author
        holder.btnRemove.setOnClickListener { onRemove(fav) }
    }

    fun setData(newList: List<FavoritePhoto>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }
}
