package com.minhnv.meme_app.ui.main.create.meme_template.export

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.minhnv.meme_app.databinding.ItemMemeIconBinding

typealias OnItemMemeSelected = (MemeIcon) -> Unit

class MemeIconAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val dataSource: MutableList<MemeIcon> = mutableListOf()
    var onItemMemeSelected: OnItemMemeSelected? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemMemeIconBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemeIconViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MemeIconViewHolder) {
            holder.bind(dataSource[position])
        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    fun set(list: MutableList<MemeIcon>) {
        dataSource.clear()
        dataSource.addAll(list)
        notifyDataSetChanged()
    }

    private inner class MemeIconViewHolder(
        private val binding: ItemMemeIconBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(memeIcon: MemeIcon) {
            binding.root.setOnClickListener {
                onItemMemeSelected?.invoke(memeIcon)
            }
            binding.imgMemeIcon.setImageResource(memeIcon.meme)
        }
    }
}