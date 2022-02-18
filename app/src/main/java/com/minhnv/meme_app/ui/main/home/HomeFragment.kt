package com.minhnv.meme_app.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.minhnv.meme_app.R
import com.minhnv.meme_app.databinding.HomeFragmentBinding
import com.minhnv.meme_app.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeFragmentBinding>() {
    override val inflaterBinding: (LayoutInflater, ViewGroup?, Boolean) -> HomeFragmentBinding
        get() = HomeFragmentBinding::inflate

    private val viewModel by viewModels<HomeViewModel>()
    private val networkDisconnectAdapter = NetworkDisconnectAdapter()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var communityAdapter: CommunityAdapter

    override var showToolbar: Boolean = false

    override fun setup() {
        linearLayoutManager = LinearLayoutManager(mActivity)
        communityAdapter = CommunityAdapter(mActivity)
        binding.rycCommunity.apply {
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            adapter = communityAdapter.withLoadStateFooter(networkDisconnectAdapter)
        }
        lifecycleScope.launchWhenStarted {
            communityAdapter.loadStateFlow.collect {
                binding.swipeCommunities.isRefreshing = it.refresh is LoadState.Loading
            }
        }
        networkDisconnectAdapter.didRetry = {
            communityAdapter.retry()
        }
        binding.swipeCommunities.setOnRefreshListener {
            communityAdapter.refresh()
        }
        lifecycleScope.launch(trackingErrorHelper.coroutineExceptionHandler()) {
            withContext(Dispatchers.Main) {
                viewModel.doGetAccessToken()
            }
            viewModel.communities.collect {
                communityAdapter.submitData(it)
            }
        }
        binding.fabSortTool.setOnClickListener {
            binding.rycCommunity.smoothScrollToPosition(0)
        }
        binding.fabAutoScroll.setOnClickListener {

        }

        binding.rycCommunity.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    doResumeOrPauseVideo(true)
                } else {
                    doResumeOrPauseVideo(false)
                }
            }
        })
    }

    private fun doResumeOrPauseVideo(isResumed: Boolean) {
        val firstIndex = linearLayoutManager.findFirstVisibleItemPosition()
        val lastIndex = linearLayoutManager.findLastVisibleItemPosition()
        for (i in firstIndex until lastIndex) {
            val viewHolder =
                binding.rycCommunity.findViewHolderForLayoutPosition(
                    i
                ) as CommunityAdapter.CommunitiesViewHolder
            if (isResumed) {
                viewHolder.didResumeVideo()
            } else {
                viewHolder.didPauseVideo()
            }
        }
    }

    override val title: Int = R.string.home
}