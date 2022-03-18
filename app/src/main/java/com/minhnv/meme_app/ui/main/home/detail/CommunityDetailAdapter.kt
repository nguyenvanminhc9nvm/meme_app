package com.minhnv.meme_app.ui.main.home.detail

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.vision.common.InputImage
import com.minhnv.meme_app.R
import com.minhnv.meme_app.data.networking.model.response.Community
import com.minhnv.meme_app.data.networking.model.response.TypeImages
import com.minhnv.meme_app.databinding.ItemCommunityDetailBinding
import com.minhnv.meme_app.databinding.ItemCommunityDetailNativeAdBinding
import com.minhnv.meme_app.ui.main.home.DidSearchTagName
import com.minhnv.meme_app.ui.main.home.TagAdapter
import com.minhnv.meme_app.utils.Constants
import com.minhnv.meme_app.utils.options.formatNumber
import com.minhnv.meme_app.utils.text_recognizer.CardType
import com.minhnv.meme_app.utils.text_recognizer.TextRecognitionProcessor
import com.minhnv.meme_app.utils.text_recognizer.TextType
import java.util.*

class CommunityDetailAdapter(
    private val context: Context
) : PagingDataAdapter<Community, RecyclerView.ViewHolder>(CommunitiesDifferent) {
    private val itemViewTypeAD = 0
    private val itemViewTypeCommunities = 1
    var didSearchTagName: DidSearchTagName? = null

    object CommunitiesDifferent : DiffUtil.ItemCallback<Community>() {

        override fun areItemsTheSame(oldItem: Community, newItem: Community): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Community, newItem: Community): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NativeAdViewHolder -> {
                holder.bind()
            }
            is CommunityDetailViewHolder -> {
                getItem(position)?.let { holder.bind(it) }
            }
            else -> throw Exception("unknown viewHolder")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            itemViewTypeAD -> NativeAdViewHolder(
                ItemCommunityDetailNativeAdBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> CommunityDetailViewHolder(
                ItemCommunityDetailBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            2 -> itemViewTypeAD
            22 -> itemViewTypeAD
            42 -> itemViewTypeAD
            62 -> itemViewTypeAD
            else -> itemViewTypeCommunities
        }
    }

    inner class NativeAdViewHolder(
        private val binding: ItemCommunityDetailNativeAdBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            AdLoader.Builder(context, Constants.ADMOB_NATIVE_ID)
                .forNativeAd { ad ->
                    binding.tvBody.text = ad.body
                    binding.imgAppIcon.setImageDrawable(ad.icon?.drawable)
                    binding.btnCallToAction.text = ad.callToAction
                    ad.mediaContent?.let {
                        binding.imgAdMedial.setImageDrawable(it.mainImage)
                    }
                    binding.tvHeadline.text = ad.headline
                    binding.ratingBar.rating = ad.starRating?.toFloat() ?: 0F
                    binding.tvAdvertiser.text = ad.advertiser
                    binding.nativeAdDetailCommunity.setNativeAd(ad)
                }
                .withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
                    }
                })
                .withNativeAdOptions(
                    NativeAdOptions.Builder()
                        .build()
                )
                .build().loadAd(AdRequest.Builder().build())
        }
    }

    inner class CommunityDetailViewHolder(
        private val binding: ItemCommunityDetailBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(community: Community) {
            val linkRes = community.images?.get(0)?.link
            when (community.images?.get(0)?.type) {
                TypeImages.VIDEO_MP4 -> {
                    binding.vvCommunityDetail.visibility = View.VISIBLE
                    binding.btnTranslate.visibility = View.GONE
                    binding.imgCommunityDetail.visibility = View.GONE
                    binding.vvCommunityDetail.setVideoPath(linkRes)
                    binding.vvCommunityDetail.start()
                    binding.vvCommunityDetail.setOnPreparedListener {
                        binding.vvCommunityDetail.start()
                    }
                }
                else -> {
                    binding.imgCommunityDetail.visibility = View.VISIBLE
                    binding.btnTranslate.visibility = View.VISIBLE
                    binding.vvCommunityDetail.visibility = View.GONE
                    Glide.with(context).load(linkRes)
                        .placeholder(R.drawable.ic_place_holder)
                        .error(R.drawable.ic_place_holder)
                        .into(binding.imgCommunityDetail)
                }
            }
            community.tags?.let {
                val tagAdapter = TagAdapter(it)
                binding.rycTags.apply {
                    layoutManager = FlexboxLayoutManager(context).apply {
                        flexWrap = FlexWrap.WRAP
                        flexDirection = FlexDirection.ROW
                    }
                    adapter = tagAdapter
                }
                tagAdapter.didSearchTag = { tagName ->
                    didSearchTagName?.invoke(tagName)
                }
            }
            community.title?.let {
                binding.tvTitleDetail.text = it
            } ?: kotlin.run {
                binding.tvTitleDetail.visibility = View.GONE
            }
            binding.btnUpDetail.text = community.ups?.let { formatNumber(it.toLong()) }
            binding.btnDownDetail.text = community.downs?.let { formatNumber(it.toLong()) }
            binding.btnViews.text = community.views?.let { formatNumber(it.toLong()) }
            binding.btnTranslate.setOnClickListener {
                Glide.with(context)
                    .asBitmap()
                    .load(linkRes)
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
                                        binding.tvResultTranslateDetail.visibility = View.VISIBLE
                                        binding.tvResultTranslateDetail.text = it
                                    }, {
                                        binding.tvResultTranslateDetail.visibility = View.INVISIBLE
                                        binding.tvResultTranslateDetail.text = it
                                    })
                                }
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })
            }
        }

        fun didPauseVideo() {
            binding.vvCommunityDetail.pause()
        }

        fun didResumeVideo() {
            binding.vvCommunityDetail.resume()
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

