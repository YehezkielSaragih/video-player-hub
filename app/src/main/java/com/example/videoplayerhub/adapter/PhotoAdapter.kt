package com.example.videoplayerhub.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.videoplayerhub.R
import com.example.videoplayerhub.model.PicsumPhoto

class PhotoAdapter(
    private val items: List<PicsumPhoto>,
    private val onClick: (PicsumPhoto) -> Unit
) : RecyclerView.Adapter<PhotoAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgThumb)
        val tvAuthor: TextView = view.findViewById(R.id.tvAuthor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_photo, parent, false)
        return VH(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val photo = items[position]
        Glide.with(holder.itemView).load("https://picsum.photos/id/${photo.id}/200/200").into(holder.img)
        holder.tvAuthor.text = photo.author
        holder.itemView.setOnClickListener { onClick(photo) }
    }
}
