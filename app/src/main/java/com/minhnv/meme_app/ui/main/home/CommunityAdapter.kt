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
import com.minhnv.meme_app.data.networking.model.response.Community
import com.minhnv.meme_app.databinding.ItemCommunityBinding


class CommunityAdapter(
    private val context: Context
) : PagingDataAdapter<Community, CommunityAdapter.CommunitiesViewHolder>(CommunitiesDifferent) {

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
            binding.btnUp.text = community.ups?.toInt().toString()
            binding.btnDown.text = community.downs?.toInt().toString()
            binding.btnSee.text = community.views?.toInt().toString()
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

    override fun onBindViewHolder(holder: CommunitiesViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunitiesViewHolder {
        val view = ItemCommunityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommunitiesViewHolder(view)
    }
}