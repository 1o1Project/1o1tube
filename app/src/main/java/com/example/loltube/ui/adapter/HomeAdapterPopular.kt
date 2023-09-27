package com.example.loltube.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loltube.R
import com.example.loltube.databinding.HomeItemBinding
import com.example.loltube.model.LOLModel

class HomeAdapterPopular(
    private val onClickItem: (Int, LOLModel) -> Unit,
) : ListAdapter<LOLModel, HomeAdapterPopular.ViewHolder>(

    object : DiffUtil.ItemCallback<LOLModel>() {
        override fun areItemsTheSame(
            oldItem: LOLModel,
            newItem: LOLModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: LOLModel,
            newItem: LOLModel
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapterPopular.ViewHolder {
        return ViewHolder(
            HomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClickItem
        )
    }

    override fun onBindViewHolder(holder: HomeAdapterPopular.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(
        private val binding: HomeItemBinding,
        private val onClickItem: (Int, LOLModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LOLModel) = with(binding) {
            Glide.with(root)
                .load(item.thumbnail)
                .into(homeThumnail)

            homeTitle.text = item.title

            container.setOnClickListener {
                onClickItem(
                    adapterPosition,
                    item)
            }
        }
    }
}