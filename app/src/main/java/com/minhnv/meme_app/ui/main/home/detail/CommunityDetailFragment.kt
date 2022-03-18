package com.minhnv.meme_app.ui.main.home.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.minhnv.meme_app.R
import com.minhnv.meme_app.databinding.CommunityDetailFragmentBinding
import com.minhnv.meme_app.ui.base.BaseFragment
import com.minhnv.meme_app.ui.main.home.HomeViewModel
import com.minhnv.meme_app.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CommunityDetailFragment : BaseFragment<CommunityDetailFragmentBinding>() {

    private val viewModel by viewModels<HomeViewModel>(ownerProducer = { requireActivity() })
    override val inflaterBinding: (LayoutInflater, ViewGroup?, Boolean) -> CommunityDetailFragmentBinding
        get() = CommunityDetailFragmentBinding::inflate

    override var showToolbar: Boolean = false
    private lateinit var communityDetailAdapter: CommunityDetailAdapter
    var newPos = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun setup() {
        communityDetailAdapter = CommunityDetailAdapter(mActivity)
        communityDetailAdapter.didSearchTagName = {
            val bundle = Bundle()
            bundle.putString(Constants.ARGUMENT_1, it)
            switchFragment(R.id.tagsFragment, bundle)
        }
        binding.rycCommunityDetail.apply {
            orientation = ViewPager2.ORIENTATION_VERTICAL
            adapter = communityDetailAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    when (state) {
                        ViewPager2.SCROLL_STATE_IDLE -> {
                            doResumeOrPauseVideo(true)
                        }
                        else -> {
                            doResumeOrPauseVideo(false)
                        }
                    }
                }

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewModel.setPosition(position)
                }
            })
        }
        lifecycleScope.launch {
            viewModel.communities.collect {
                communityDetailAdapter.submitData(it)
            }
        }
        viewModel.position.value?.let {
            newPos = it
            (binding.rycCommunityDetail[0] as RecyclerView).scrollToPosition(
                it
            )
        }
    }

    private fun doResumeOrPauseVideo(isResumed: Boolean) {
        val layoutManager =
            (binding.rycCommunityDetail[0] as RecyclerView).layoutManager as LinearLayoutManager
        val firstIndex = layoutManager.findFirstVisibleItemPosition()
        val lastIndex = layoutManager.findLastVisibleItemPosition()
        for (i in firstIndex until lastIndex) {
            val viewHolder =
                (binding.rycCommunityDetail[0] as RecyclerView).findViewHolderForLayoutPosition(
                    i
                ) as? CommunityDetailAdapter.CommunityDetailViewHolder
            if (isResumed) {
                viewHolder?.didResumeVideo()
            } else {
                viewHolder?.didPauseVideo()
            }
        }
    }

    override val title: Int
        get() = R.string.description

}