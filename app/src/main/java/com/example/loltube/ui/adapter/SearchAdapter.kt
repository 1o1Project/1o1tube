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
import com.example.loltube.R
import com.example.loltube.databinding.SearchItemBinding
import com.example.loltube.model.LOLModel


// mContext 사용하지 않는다 혀 context로 변경
class SearchAdapter(private val context: Context) : RecyclerView.Adapter<SearchAdapter.ItemViewHolder>() {
    var items = ArrayList<LOLModel>()

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

        Glide.with(context)
            .load(currentItem.thumbnail)
            .placeholder(R.drawable.ic_downloading)
            .into(holder.thumbNailImage)


        holder.title.text = currentItem.title

    }

    inner class ItemViewHolder(var binding: SearchItemBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        var thumbNailImage: ImageView = binding.searchThumbnail
        var title: TextView = binding.searchTitle
        var thumbNailItem: ConstraintLayout = binding.thumbnailItem

        init {
            thumbNailImage.setOnClickListener(this)
            thumbNailItem.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val position = adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return
            val item = items[position]

            // 클릭한 아이템을 인터페이스를 통해 SearchFragment로 전달
            itemClickListener?.onItemClick(item)
        }
    }

    // 인터페이스 정의
    interface OnItemClickListener {
        fun onItemClick(item: LOLModel)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }


}