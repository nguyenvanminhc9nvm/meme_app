package com.minhnv.meme_app.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.minhnv.meme_app.R
import com.minhnv.meme_app.databinding.HomeFragmentBinding
import com.minhnv.meme_app.ui.base.BaseFragment
import com.minhnv.meme_app.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeFragmentBinding>() {
    override val inflaterBinding: (LayoutInflater, ViewGroup?, Boolean) -> HomeFragmentBinding
        get() = HomeFragmentBinding::inflate

    private val viewModel by viewModels<HomeViewModel>(ownerProducer = { requireActivity() })
    private val networkDisconnectAdapter = NetworkDisconnectAdapter()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var communityAdapter: CommunityAdapter
    private var duration = 1000L
    override var showToolbar: Boolean = false
    private val job: Job = Job()

    override fun setup() {
        println("#viewModel: $viewModel")
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
        binding.progressAutoScroll.mMax = 4

        binding.progressAutoScroll.setOnClickListener {
            duration += 1000L
            job.cancelChildren()
            Toast.makeText(
                mActivity,
                "${getString(R.string.auto_scroll_mes)} ${TimeUnit.MILLISECONDS.toSeconds(duration)} ${
                    getString(
                        R.string.auto_scroll_mes_second
                    )
                }",
                Toast.LENGTH_SHORT
            ).show()
            CoroutineScope(Dispatchers.IO + job).launch {
                flow {
                    var counter: Long = 0
                    while (true) {
                        binding.progressAutoScroll.startAnimator(
                            duration = duration,
                            start = 0,
                            end = 4,
                            repeatCount = counter.toInt()
                        )
                        delay(duration)
                        emit(counter++)
                    }
                }.flowOn(Dispatchers.Main).collect {
                    binding.rycCommunity.smoothScrollBy(0, 1000)
                    binding.progressAutoScroll.mProgress = it.toInt()
                }
            }
        }

        binding.rycCommunity.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        doResumeOrPauseVideo(true)
                    }
                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                        didStopAutoScroll()
                    }
                    else -> {
                        didSaveItemReadByUser()
                        doResumeOrPauseVideo(false)
                    }
                }
            }
        })
        communityAdapter.didSearchTagName = {
            val bundle = Bundle()
            bundle.putString(Constants.ARGUMENT_1, it)
            switchFragment(R.id.tagsFragment, bundle)
        }

        binding.btnSort.didSelectedSortListCommunities = { section, sort, day ->
            lifecycleScope.launch {
                viewModel.didLoadCommunities(section, sort, day).collect {
                    communityAdapter.submitData(it)
                }
                linearLayoutManager.scrollToPosition(0)
            }
        }
        communityAdapter.didSelectedItem = {
            viewModel.setPosition(it)
            switchFragment(R.id.communityDetailFragment)
        }
        viewModel.position.observe(viewLifecycleOwner) {
            linearLayoutManager.scrollToPosition(it)
        }
    }

    private fun didStopAutoScroll() {
        job.cancelChildren()
        duration = 1000
        binding.progressAutoScroll.stopAnimator()
        binding.progressAutoScroll.mProgress = 0
    }

    private fun didSaveItemReadByUser() {
        val index = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
        if (index != -1) {

        }
    }

    private fun doResumeOrPauseVideo(isResumed: Boolean) {
        val firstIndex = linearLayoutManager.findFirstVisibleItemPosition()
        val lastIndex = linearLayoutManager.findLastVisibleItemPosition()
        for (i in firstIndex until lastIndex) {
            val viewHolder =
                binding.rycCommunity.findViewHolderForLayoutPosition(
                    i
                ) as? CommunityAdapter.CommunitiesViewHolder
            if (isResumed) {
                viewHolder?.didResumeVideo()
            } else {
                viewHolder?.didPauseVideo()
            }
        }
    }

    override val title: Int = R.string.home
}
