package com.example.loltube.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loltube.databinding.FavoriteItemBinding
import com.example.loltube.model.LOLModel


class FavoriteListAdapter : RecyclerView.Adapter<FavoriteListAdapter.ViewHolder>() {

    private var favoriteList = mutableListOf<LOLModel>()

    class ViewHolder (private val binding : FavoriteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite: LOLModel) {
            Glide.with(binding.root.context)
                .load(favorite.thumbnail)
                .into(binding.favoriteThumbnailImage)
            binding.mypageItemTitle.text = favorite.title
        }
    }

    fun setList(favoriteList : MutableList<LOLModel>) {
        this.favoriteList = favoriteList
        notifyItemRangeChanged(0, this.favoriteList.size)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FavoriteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    

    override fun getItemCount(): Int {
        return favoriteList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(favoriteList[position])
    }
}