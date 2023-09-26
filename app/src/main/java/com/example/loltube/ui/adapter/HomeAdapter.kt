package com.example.loltube.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loltube.databinding.HomeItemBinding
import com.example.loltube.ui.fragment.Home.HomeModel

class HomeAdapter(
    private val onClickItem: (Int, HomeModel) -> Unit,
) : ListAdapter<HomeModel, HomeAdapter.ViewHolder>(

    object : DiffUtil.ItemCallback<HomeModel>() {
        override fun areItemsTheSame(
            oldItem: HomeModel,
            newItem: HomeModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: HomeModel,
            newItem: HomeModel
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.ViewHolder {
        return ViewHolder(
            HomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClickItem
        )
    }

    override fun onBindViewHolder(holder: HomeAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(
        private val binding: HomeItemBinding,
        private val onClickItem: (Int, HomeModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HomeModel) = with(binding) {
            Glide.with(root)
                .load(item.thumnail)
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