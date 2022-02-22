package com.minhnv.meme_app.ui.main.deep

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.minhnv.meme_app.R
import com.minhnv.meme_app.data.networking.model.response.Images
import com.minhnv.meme_app.databinding.ItemDeepBinding

class DeepAdapter(
    private val context: Context
) : PagingDataAdapter<Images, DeepAdapter.DeepViewHolder>(DeepDifferent) {

    object DeepDifferent : DiffUtil.ItemCallback<Images>() {

        override fun areItemsTheSame(oldItem: Images, newItem: Images): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Images, newItem: Images): Boolean {
            return oldItem == newItem
        }
    }

    inner class DeepViewHolder(
        private val binding: ItemDeepBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(images: Images) {
            Glide.with(context).load(images.link)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder).into(binding.imgDeep)
            val visibility = if (images.title.isNullOrEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
            val visibilityDesc = if (images.description.isNullOrEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
            binding.tvTitleImages.visibility = visibility
            binding.tvDescriptionImages.visibility = visibilityDesc
            binding.tvTitleImages.text = images.title
            binding.tvDescriptionImages.text = images.description
            binding.tvViewsDeep.text = String.format("${images.views?.toInt()} views")
            val imgDrawable = when (images.views?.toInt() ?: 0) {
                in 0..10 -> {
                    R.drawable.ic_see_10
                }
                in 11..100 -> {
                    R.drawable.ic_see_100
                }
                in 101..1000 -> {
                    R.drawable.ic_see_1000
                }
                in 1001..10000 -> {
                    R.drawable.ic_see_10000
                }
                in 10001..100000 -> {
                    R.drawable.ic_see_100000
                }
                else -> R.drawable.ic_see_500000
            }
            binding.imgViewsDeep.setImageResource(imgDrawable)
        }
    }

    override fun onBindViewHolder(holder: DeepViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeepViewHolder {
        val view = ItemDeepBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeepViewHolder(view)
    }
}