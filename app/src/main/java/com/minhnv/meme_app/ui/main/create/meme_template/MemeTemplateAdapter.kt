package com.minhnv.meme_app.ui.main.create.meme_template

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.minhnv.meme_app.data.networking.model.local.MemeTemplate
import com.minhnv.meme_app.databinding.ItemMemeTemplateBinding

typealias OnItemSelected = (MemeTemplate) -> Unit

class MemeTemplateAdapter(
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val memeTemplates = mutableListOf<MemeTemplate>()
    var onItemSelected: OnItemSelected? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemMemeTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemeTemplateViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MemeTemplateViewHolder) {
            holder.bind(memeTemplates[position])
        }
    }

    override fun getItemCount(): Int {
        return memeTemplates.size
    }

    fun set(list: MutableList<MemeTemplate>) {
        memeTemplates.clear()
        memeTemplates.addAll(list)
        notifyDataSetChanged()
    }

    inner class MemeTemplateViewHolder(
        private val binding: ItemMemeTemplateBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(memeTemplate: MemeTemplate) {
            binding.root.setOnClickListener {
                onItemSelected?.invoke(memeTemplate)
            }
            Glide.with(context).load(memeTemplate.memeUrl).listener(object : RequestListener<Drawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.tvLoading.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.tvLoading.visibility = View.GONE
                    return false
                }

            }).into(binding.imgMeme)
        }
    }
}