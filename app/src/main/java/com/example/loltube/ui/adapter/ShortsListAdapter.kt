package com.example.loltube.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.loltube.databinding.ShortsItemBinding
import com.example.loltube.model.Items
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class ShortsListAdapter(private val loadVideo : (YouTubePlayerView, Items) -> Unit) : RecyclerView.Adapter<ShortsListAdapter.ViewHolder>() {

    private var shortList: List<Items> = mutableListOf()

    class ViewHolder(private val binding: ShortsItemBinding, private val loadVideo: (YouTubePlayerView, Items) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(items: Items) {
            loadVideo(binding.shortsView, items)
        }
    }

    fun setList(shortList: List<Items>) {
        this.shortList = shortList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ShortsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), loadVideo)
    }

    override fun getItemCount(): Int {
        return shortList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(shortList[position])
    }
}