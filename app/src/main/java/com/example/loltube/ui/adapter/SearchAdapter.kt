package com.example.loltube.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loltube.databinding.SearchItemBinding
import com.example.loltube.model.SearchItemModel

class SearchAdapter(private val mContext: Context) : RecyclerView.Adapter<SearchAdapter.ItemViewHolder>() {
    var items = ArrayList<SearchItemModel>()

    fun itemClear() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = items[position]

        Glide.with(mContext)
            .load(currentItem.url)
            .into(holder.thumbNailImage)

        holder.title.text = currentItem.title

    }

    inner class ItemViewHolder(binding: SearchItemBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        var thumbNailImage: ImageView = binding.searchThumbnail
        var title: TextView = binding.searchTitle
        var thumbNailItem: ConstraintLayout = binding.thumbnailItem

        init {
            thumbNailImage.setOnClickListener(this)
            thumbNailItem.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val position = adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return
            notifyItemChanged(position)
        }
    }
}