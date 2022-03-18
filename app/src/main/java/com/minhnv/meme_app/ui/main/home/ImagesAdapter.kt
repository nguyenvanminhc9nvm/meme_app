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
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer
import com.minhnv.meme_app.R
import com.minhnv.meme_app.data.networking.model.response.Images
import com.minhnv.meme_app.data.networking.model.response.TypeImages.IMAGES_GIF
import com.minhnv.meme_app.data.networking.model.response.TypeImages.VIDEO_MP4
import com.minhnv.meme_app.databinding.ItemImagesAdapterBinding
import com.minhnv.meme_app.utils.text_recognizer.CardType
import com.minhnv.meme_app.utils.text_recognizer.TextRecognitionProcessor
import com.minhnv.meme_app.utils.text_recognizer.TextType
import java.util.*

typealias DidDetectionTextError = () -> Unit

class ImagesAdapter(
    private val context: Context,
    private val dataSource: MutableList<Images>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var detector: TextRecognizer
    var didDetectionTextError: DidDetectionTextError? = null
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
                    binding.tvResultTranslate.visibility = View.GONE
                    glideContext.asGif().load(images.link)
                        .placeholder(R.drawable.ic_place_holder)
                        .error(R.drawable.ic_place_holder)
                        .into(binding.imgImages)
                }
                VIDEO_MP4 -> {
                    binding.imgImages.visibility = View.GONE
                    binding.constraintVideo.visibility = View.VISIBLE
                    binding.btnTranslate.visibility = View.GONE
                    binding.tvResultTranslate.visibility = View.GONE
                    binding.vvPreview.setVideoPath(images.link)
                    binding.vvPreview.start()
                    binding.vvPreview.setOnPreparedListener {
                        binding.vvPreview.start()
                    }
                    hasVideo = true
                }
                else -> {
                    binding.imgImages.visibility = View.VISIBLE
                    binding.constraintVideo.visibility = View.GONE
                    binding.btnTranslate.visibility = View.VISIBLE
                    binding.tvResultTranslate.visibility = View.VISIBLE
                    glideContext.load(images.link).apply(RequestOptions().override(1000, 1000))
                        .placeholder(R.drawable.ic_place_holder)
                        .error(R.drawable.ic_place_holder)
                        .into(binding.imgImages)
                }
            }

            binding.btnTranslate.setOnClickListener {
                glideContext.asBitmap()
                    .error(R.drawable.ic_place_holder).load(images.link)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            val textRecognitionProcessor = TextRecognitionProcessor.Builder()
                                .setContext(context)
                                .setCardType(CardType.CARD_TYPE_1)
                                .setLangType(TextType.TEXT_RECOGNITION_JP)
                                .build()
                            textRecognitionProcessor.detect(InputImage.fromBitmap(resource, 0))
                                .addOnCanceledListener {

                                }.addOnSuccessListener { result ->
                                    didTranslateText(result.text, {
                                        binding.tvResultTranslate.text = it
                                    }, {
                                        binding.tvResultTranslate.text = it
                                    })
                                }
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })
            }
        }

        private fun didTranslateText(
            value: String,
            onSuccess: (String) -> Unit,
            onFailed: (String) -> Unit
        ) {
            val languageIdentifier = LanguageIdentification.getClient()
            languageIdentifier.identifyLanguage(value)
                .addOnSuccessListener { languageCode ->
                    if (languageCode == "und") {
                        onFailed.invoke(context.getString(R.string.cant_identify_language))
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
                                englishGermanTranslator.translate(value)
                                    .addOnSuccessListener {
                                        onSuccess.invoke(it)
                                    }.addOnFailureListener {
                                        onFailed.invoke(context.getString(R.string.cant_identify_language))
                                    }
                            }
                            .addOnFailureListener { exception ->
                                onFailed.invoke(exception.message ?: "")
                            }
                    }
                }
                .addOnFailureListener {
                    onFailed.invoke(it.message ?: "")
                }
        }
    }
}