package com.minhnv.meme_app.ui.main.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextRecognizer
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.minhnv.meme_app.R
import com.minhnv.meme_app.data.networking.model.response.Images
import com.minhnv.meme_app.data.networking.model.response.TypeImages.IMAGES_GIF
import com.minhnv.meme_app.data.networking.model.response.TypeImages.VIDEO_MP4
import com.minhnv.meme_app.databinding.ItemImagesAdapterBinding
import java.util.*

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

        private var hasVideo = false

        fun didPauseVideo() {
            if (hasVideo) {
                binding.vvPreview.pause()
            }
        }

        fun didResumeVideo() {
            if (hasVideo) {
                binding.vvPreview.start()
            }
        }

        fun bind(images: Images) {
            val glideContext = Glide.with(context)
            when (images.type) {
                IMAGES_GIF -> {
                    binding.imgImages.visibility = View.VISIBLE
                    binding.constraintVideo.visibility = View.GONE
                    binding.btnTranslate.visibility = View.VISIBLE
                    glideContext.asGif().load(images.link).placeholder(R.drawable.ic_place_holder)
                        .into(binding.imgImages)
                }
                VIDEO_MP4 -> {
                    binding.imgImages.visibility = View.GONE
                    binding.constraintVideo.visibility = View.VISIBLE
                    binding.btnTranslate.visibility = View.GONE
                    binding.vvPreview.setVideoPath(images.link)
                    binding.vvPreview.start()
                    hasVideo = true
                }
                else -> {
                    binding.imgImages.visibility = View.VISIBLE
                    binding.constraintVideo.visibility = View.GONE
                    binding.btnTranslate.visibility = View.VISIBLE
                    glideContext.load(images.link).placeholder(R.drawable.ic_place_holder)
                        .into(binding.imgImages)
                }
            }

            glideContext.load(images.link).apply(RequestOptions().override(1000, 1000))
                .into(binding.imgImages)
            var bitmap: Bitmap? = null

            glideContext.asBitmap().load(images.link)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        bitmap = resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })

            binding.btnTranslate.setOnClickListener {
                detector = TextRecognizer.Builder(context).build()
                if (detector.isOperational) {
                    val frame = Frame.Builder().setBitmap(bitmap!!).build()
                    val textBlocks = detector.detect(frame)
                    var blocks = ""
                    for (i in 0 until textBlocks.size()) {
                        val tBlock = textBlocks.valueAt(i)
                        (blocks + tBlock.value + "\n").also { blocks = it }
                    }
                    if (textBlocks.size() == 0) {
                        binding.btnTranslate.visibility = View.GONE
                        binding.tvResultTranslate.visibility = View.GONE
                    } else {
                        binding.btnTranslate.visibility = View.VISIBLE
                        binding.tvResultTranslate.visibility = View.VISIBLE
                        val result = " $blocks "
                        val languageIdentifier = LanguageIdentification.getClient()
                        languageIdentifier.identifyLanguage(result)
                            .addOnSuccessListener { languageCode ->
                                if (languageCode == "und") {
                                    binding.tvResultTranslate.text =
                                        context.getText(R.string.cant_identify_language)
                                } else {
                                    val options = TranslatorOptions.Builder()
                                        .setSourceLanguage(languageCode)
                                        .setTargetLanguage(Locale.getDefault().language)
                                        .build()
                                    val englishGermanTranslator = Translation.getClient(options)
                                    val conditions = DownloadConditions.Builder()
                                        .requireWifi()
                                        .build()
                                    englishGermanTranslator.downloadModelIfNeeded(conditions)
                                        .addOnSuccessListener {
                                            englishGermanTranslator.translate(result)
                                                .addOnSuccessListener {
                                                    binding.tvResultTranslate.visibility = View.VISIBLE
                                                    binding.tvResultTranslate.text = it + "\n"
                                                }.addOnFailureListener {
                                                    binding.tvResultTranslate.text =
                                                        context.getText(R.string.cant_identify_language)
                                                }
                                        }
                                        .addOnFailureListener { exception ->
                                            binding.tvResultTranslate.text = exception.message
                                        }
                                }
                            }
                            .addOnFailureListener {
                                binding.tvResultTranslate.text = it.message
                            }
                    }
                }
            }
        }
    }
}