package com.minhnv.meme_app.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.minhnv.meme_app.data.networking.model.response.Tags
import com.minhnv.meme_app.databinding.ItemTagsBinding

typealias DidSearchTag = (String) -> Unit

class TagAdapter(
    private val dataSource: MutableList<Tags>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var didSearchTag: DidSearchTag? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemTagsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TagViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TagViewHolder) {
            holder.bind(dataSource[position])
        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    inner class TagViewHolder(
        private val binding: ItemTagsBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(tags: Tags) {
            binding.tvNameTags.text = String.format("#${tags.name}")
            binding.root.setOnClickListener {
                didSearchTag?.invoke(tags.name ?: "")
            }
        }
    }
}