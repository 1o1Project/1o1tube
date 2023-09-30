package com.example.loltube.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.loltube.databinding.ShortsItemBinding
import com.example.loltube.model.Items
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class ShortsListAdapter(private val lifecycle: androidx.lifecycle.Lifecycle) : RecyclerView.Adapter<ShortsListAdapter.ViewHolder>() {

    private var shortList: List<Items> = mutableListOf()

    class ViewHolder(binding: ShortsItemBinding, private val lifecycle: androidx.lifecycle.Lifecycle) : RecyclerView.ViewHolder(binding.root) {
        private var youTubePlayer: YouTubePlayer? = null
        private var currentVideoId: String? = null
        init {
            val youtubePlayerView = binding.shortsView
            lifecycle.addObserver(youtubePlayerView)

            youtubePlayerView.addYouTubePlayerListener(object: AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    super.onReady(youTubePlayer)
                    this@ViewHolder.youTubePlayer = youTubePlayer
                    currentVideoId?.let { youTubePlayer.loadVideo(it, 0f) }
                }
            })
        }
        fun bind(items: Items) {
            currentVideoId = items.id.videoId
            youTubePlayer?.loadVideo(items.id.videoId, 0f)
        }
    }

    fun setList(shortList: List<Items>) {
        this.shortList = shortList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ShortsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), lifecycle)
    }

    override fun getItemCount(): Int {
        return shortList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(shortList[position])
    }
}