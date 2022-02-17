package com.minhnv.meme_app.ui.main.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextRecognizer
import com.minhnv.meme_app.data.networking.model.response.Images
import com.minhnv.meme_app.databinding.ItemImagesAdapterBinding

class ImagesAdapter(
    private val context: Context,
    private val dataSource: MutableList<Images>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var detector: TextRecognizer
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            ItemImagesAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ImagesViewHolder) {
            holder.bind(dataSource[position])
        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    inner class ImagesViewHolder(
        private val binding: ItemImagesAdapterBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(images: Images) {
            Glide.with(context).asBitmap().load(images.link)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        binding.imgImages.setImageBitmap(resource)
                        detector = TextRecognizer.Builder(context).build()
                        if (detector.isOperational) {
                            val frame = Frame.Builder().setBitmap(resource).build()
                            val textBlocks = detector.detect(frame)
                            var blocks = ""
                            for (i in 0 until textBlocks.size()) {
                                val tBlock = textBlocks.valueAt(i)
                                (blocks + tBlock.value + "\n").also { blocks = it }
                            }
                            if (textBlocks.size() == 0) {
                                binding.tvResultTranslate.text =
                                    "Scan Failed: Found nothing to scan";
                            } else {
                                binding.tvResultTranslate.text = blocks + "\n"
                            }
                        }
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })
        }
    }
}