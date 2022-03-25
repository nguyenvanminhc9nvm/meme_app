package com.minhnv.meme_app.ui.main.deep

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.minhnv.meme_app.R
import com.minhnv.meme_app.data.networking.model.response.Images
import com.minhnv.meme_app.databinding.AdUnifiedBinding
import com.minhnv.meme_app.databinding.BannerAdviewBinding
import com.minhnv.meme_app.databinding.ItemDeepBinding
import com.minhnv.meme_app.utils.Constants

class DeepAdapter(
    private val context: Context
) : PagingDataAdapter<Images, RecyclerView.ViewHolder>(DeepDifferent) {

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
                .apply(RequestOptions().override(1000, 1000))
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
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
            val views = images.views?.toInt() ?: 0
            binding.tvViewsDeep.text = String.format("$views views")
            val imgDrawable = when (views) {
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

    inner class NativeAdLoadedViewHolder(
        private val binding: AdUnifiedBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            val adRequest = AdRequest.Builder().build()
            val adLoader = AdLoader.Builder(context, Constants.ADMOB_NATIVE_ID)
                .forNativeAd { ad ->
                    binding.adBody.text = ad.body
                    binding.adAppIcon.setImageDrawable(ad.icon?.drawable)
                    binding.adPrice.text = ad.price
                    binding.adCallToAction.text = ad.callToAction
                    binding.adStore.text = ad.store
                    ad.mediaContent?.let {
                        binding.adMedia.visibility = View.VISIBLE
                        binding.adMedia.setImageDrawable(it.mainImage)
                    } ?: kotlin.run {
                        binding.adMedia.visibility = View.GONE
                    }
                    binding.adHeadline.text = ad.headline
                    binding.adStars.rating = ad.starRating?.toFloat() ?: 0F
                    binding.adAdvertiser.text = ad.advertiser
                    binding.nativeAdView.setNativeAd(ad)
                }
                .withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
                    }
                })
                .withNativeAdOptions(
                    NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build()
                )
                .build()

            adLoader.loadAd(adRequest)
        }
    }

    private val itemViewTypeAD = 0
    private val itemViewTypeBannerAD = 2
    private val itemViewTypeDeep = 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NativeAdLoadedViewHolder -> {
                holder.bind()
            }
            is BannerAdViewHolder -> {
                holder.bind()
            }
            is DeepViewHolder -> {
                getItem(position)?.let { holder.bind(it) }
            }
            else -> throw Exception("unknown viewHolder")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            itemViewTypeAD -> NativeAdLoadedViewHolder(
                AdUnifiedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            itemViewTypeBannerAD -> BannerAdViewHolder(
                BannerAdviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> DeepViewHolder(
                ItemDeepBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            2 -> itemViewTypeBannerAD
            12 -> itemViewTypeBannerAD
            22 -> itemViewTypeAD
            32 -> itemViewTypeAD
            35 -> itemViewTypeBannerAD
            42 -> itemViewTypeAD
            52 -> itemViewTypeAD
            55 -> itemViewTypeBannerAD
            62 -> itemViewTypeAD
            72 -> itemViewTypeBannerAD
            82 -> itemViewTypeAD
            92 -> itemViewTypeBannerAD
            102 -> itemViewTypeAD
            112 -> itemViewTypeBannerAD
            else -> itemViewTypeDeep
        }
    }

    inner class BannerAdViewHolder(
        private val binding: BannerAdviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val adView = AdView(context)
            adView.adSize = AdSize.BANNER
            adView.adUnitId = Constants.BANNER_ID
            val ad = AdRequest.Builder().build()
            adView.loadAd(ad)
            binding.adView.addView(adView)
        }
    }
}