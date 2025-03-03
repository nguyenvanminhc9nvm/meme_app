package com.minhnv.meme_app.ui.main.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.minhnv.meme_app.data.networking.model.response.Community
import com.minhnv.meme_app.databinding.AdUnifiedBinding
import com.minhnv.meme_app.databinding.BannerAdviewBinding
import com.minhnv.meme_app.databinding.ItemCommunityBinding
import com.minhnv.meme_app.utils.Constants
import com.minhnv.meme_app.utils.options.formatNumber

typealias DidSearchTagName = (String) -> Unit
typealias DidSelectedItem = (Int) -> Unit

class CommunityAdapter(
    private val context: Context
) : PagingDataAdapter<Community, RecyclerView.ViewHolder>(CommunitiesDifferent) {
    private val itemViewTypeAD = 0
    private val itemViewTypeBannerAD = 2
    private val itemViewTypeCommunities = 1
    var didSearchTagName: DidSearchTagName? = null
    var didSelectedItem: DidSelectedItem? = null

    object CommunitiesDifferent : DiffUtil.ItemCallback<Community>() {

        override fun areItemsTheSame(oldItem: Community, newItem: Community): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Community, newItem: Community): Boolean {
            return oldItem == newItem
        }
    }

    inner class CommunitiesViewHolder(
        private val binding: ItemCommunityBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val layoutManagerImages = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        fun bind(community: Community) {
            binding.root.setOnClickListener {
                didSelectedItem?.invoke(layoutPosition)
            }
            val snapHelper = PagerSnapHelper()
            binding.rycImage.onFlingListener = null
            snapHelper.attachToRecyclerView(binding.rycImage)
            community.images?.let {
                val imagesAdapter = ImagesAdapter(context, community.images)
                binding.rycImage.apply {
                    layoutManager = layoutManagerImages
                    setHasFixedSize(true)
                    adapter = imagesAdapter
                }
            }
            community.tags?.let {
                val tagAdapter = TagAdapter(it)
                binding.rycTag.apply {
                    layoutManager = FlexboxLayoutManager(context).apply {
                        flexWrap = FlexWrap.WRAP
                        flexDirection = FlexDirection.ROW
                    }
                    adapter = tagAdapter
                }
                tagAdapter.didSearchTag = { nameTag ->
                    didSearchTagName?.invoke(nameTag)
                }
            }
            binding.linearUp.setOnClickListener {
                var upCount = community.ups?.toInt()
                upCount = upCount?.plus(1)
                binding.btnUp.text = upCount.toString()
            }

            binding.linearDown.setOnClickListener {
                var downCount = community.downs?.toInt()
                downCount = downCount?.minus(1)
                binding.btnDown.text = downCount.toString()
            }

            community.title?.let {
                binding.tvTitle.text = it
            } ?: kotlin.run {
                binding.tvTitle.visibility = View.GONE
            }
            community.description?.let {
                binding.tvDescription.text = it
            } ?: kotlin.run {
                binding.tvDescription.visibility = View.GONE
            }
            binding.btnUp.text = community.ups?.let { formatNumber(it.toLong()) }
            binding.btnDown.text = community.downs?.let { formatNumber(it.toLong()) }
            binding.btnSee.text = community.views?.let { formatNumber(it.toLong()) }
        }

        fun didPauseVideo() {
            val firstIndex = layoutManagerImages.findFirstCompletelyVisibleItemPosition()
            val videoViewHolder = binding.rycImage.findViewHolderForLayoutPosition(
                firstIndex
            ) as? ImagesAdapter.ImagesViewHolder
            videoViewHolder?.didPauseVideo()
        }

        fun didResumeVideo() {
            val firstIndex = layoutManagerImages.findFirstCompletelyVisibleItemPosition()
            val videoViewHolder = binding.rycImage.findViewHolderForLayoutPosition(
                firstIndex
            ) as? ImagesAdapter.ImagesViewHolder
            videoViewHolder?.didResumeVideo()
        }
    }

    inner class NativeAdLoadedViewHolder(
        private val binding: AdUnifiedBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            AdLoader.Builder(context, Constants.ADMOB_NATIVE_ID)
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
                        .build()
                )
                .build().loadAd(AdRequest.Builder().build())
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            2 -> itemViewTypeBannerAD
            12 -> itemViewTypeBannerAD
            22 -> itemViewTypeAD
            35 -> itemViewTypeAD
            42 -> itemViewTypeBannerAD
            55 -> itemViewTypeAD
            62 -> itemViewTypeAD
            72 -> itemViewTypeBannerAD
            82 -> itemViewTypeAD
            92 -> itemViewTypeAD
            102 -> itemViewTypeBannerAD
            else -> itemViewTypeCommunities
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NativeAdLoadedViewHolder -> {
                holder.bind()
            }
            is BannerAdViewHolder -> {
                holder.bind()
            }
            is CommunitiesViewHolder -> {
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
            else -> CommunitiesViewHolder(
                ItemCommunityBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
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